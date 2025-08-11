package com.example.buituananh.presentation.playlist.detail_playlist_component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.presentation.components.MusicAnimation
import com.example.buituananh.util.formatToString

@Composable
fun LinearSongItem(
    startSong: () -> Unit,
    modifier: Modifier = Modifier,
    song: Song,
    isSortMode: Boolean,
    playedSong: Song? = null,
    onClick: (Pair<Offset, Song>) -> Unit,
) {

    var iconOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { startSong() }
            .then(
                if (playedSong == song) {
                    Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)
                } else {
                    Modifier.background(MaterialTheme.colorScheme.surface)
                },
            )
            .padding(0.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            
    ) {

        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.imageUri)
                    .crossfade(true)
                    .error(R.drawable.default_song)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.Center),
            )
            if (playedSong == song) {
                MusicAnimation(Modifier.size(50.dp).align(Alignment.Center))
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = song.title ?: "null",
                fontWeight = FontWeight.W500,
                letterSpacing = 1.5.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                minLines = 1,
                modifier = Modifier.basicMarquee(),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = song.artist ?: "null",
                fontWeight = FontWeight.W500,
                letterSpacing = 1.5.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .alpha(0.6f)
                    .basicMarquee(),
            )
        }
        Spacer(Modifier.width(3.dp))
        Text(
            text = song.duration?.formatToString() ?: "00:00",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = {
                if (!isSortMode) onClick(iconOffset to song)
            },
            modifier = Modifier.then(
                if (!isSortMode) {
                    Modifier.onGloballyPositioned { coords ->
                        val offset = coords.localToWindow(Offset.Zero)
                        iconOffset = offset
                    }
                } else {
                    Modifier
                },
            ),
        ) {
            Icon(
                imageVector = if (!isSortMode) Icons.Default.MoreVert else Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp),
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
