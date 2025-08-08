package com.example.buituananh.presentation.library.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.util.formatToString

@Composable
fun SongItem(
    song: Song,
    clickSongOptions: () -> Unit,
    shareSong: () -> Unit,
    playSong: () -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val sizeInPx = with(LocalDensity.current) { 60.dp.roundToPx() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(horizontal = 16.dp)
            .clickable { playSong() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.imageUri)
                .size(sizeInPx)
                .crossfade(true)
                .error(R.drawable.default_song)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = song.title ?: "null",
                fontWeight = FontWeight.W500,
                letterSpacing = 1.5.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                minLines = 1,
                modifier = Modifier.basicMarquee()
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = song.artist ?: "null",
                fontWeight = FontWeight.W500,
                letterSpacing = 1.5.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alpha(0.6f).basicMarquee(),
            )
        }
        Spacer(Modifier.width(3.dp))
        Text(
            text = song.duration?.formatToString() ?: "00:00",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = {
                expanded = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            SongOptionsMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                onAddToPlaylistClick = {
                    expanded = false
                    clickSongOptions()
                },
                onShareClick = {
                    expanded = false
                    shareSong()
                }
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewLinearSongItem(modifier: Modifier = Modifier) {
//    BuiTuanAnhTheme {
//        LinearSongItem(
//            song = Song(
//                name = "graindy days",
//                author = "moody.",
//                duration = 4 to 30,
//                imageId = R.drawable.pic1,
//                id = 0
//            )
//        ) { }
//    }

}
