package com.example.buituananh.presentation.playlist.playlist_item

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buituananh.R
import com.example.buituananh.model.Playlist
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    removePlaylist: () -> Unit,
    renamePlaylist: () -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val sizeInPx = with(LocalDensity.current) { 70.dp.roundToPx() }

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(start = 14.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(playlist.imageBitmap)
                .crossfade(true)
                .error(R.drawable.default_song)
                .size(sizeInPx)
                .build(),
            contentDescription = playlist.title,
            modifier = Modifier.size(70.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = playlist.title,
                fontWeight = FontWeight.W600,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.basicMarquee()
            )
            Text(
                text = "${playlist.songs.size} songs",
                fontWeight = FontWeight.W500,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(Modifier
            .weight(1f)
            .padding(end = 6.dp))
        IconButton(
            onClick = {
                expanded = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )
            PlaylistOptionMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                onRemovePlaylistClick = removePlaylist
            ) {
                renamePlaylist()
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PlaylistItemPreview(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        PlaylistItem(
            playlist = Playlist(title = "Playlits 1"),
            removePlaylist = {}
        ) {

        }
    }

}