package com.example.buituananh.presentation.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buituananh.R
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun ChoosePlaylistDialog(
    modifier: Modifier = Modifier,
    playlists: List<Playlist>,
    onAddNewPlaylist: () -> Unit,
    onAddToPlaylist: (Playlist) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 250.dp, max = 500.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.choose_playlist),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if(playlists.isEmpty()) {
            Spacer(Modifier.height(50.dp))
            Text(
                text = """
                    ${stringResource(R.string.no_playlist_line1)}
                    ${stringResource(R.string.no_playlist_line2)}
                    ${stringResource(R.string.no_playlist_line3)}
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.shapes.large
                    )
                    .clickable {
                        onAddNewPlaylist()
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(Modifier.height(60.dp))

        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(Modifier.height(14.dp))
                }
                items(playlists) { playlist ->
                    Row(
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .fillMaxWidth()
                            .padding(start = 14.dp, end = 8.dp)
                            .clickable {
                                onAddToPlaylist(playlist)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(playlist.imageUri)
                                .crossfade(true)
                                .error(R.drawable.default_song)
                                .size(100)
                                .build(),
                            contentDescription = playlist.title,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = playlist.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                minLines = 1,
                                maxLines = 1,
                                modifier = Modifier.basicMarquee()
                            )
                            Text(
                                text = "${playlist.songs.size} ${stringResource(R.string.songs)}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(
                            Modifier
                                .weight(1f)
                                .padding(end = 6.dp)
                        )
                    }

                }
                item {
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewChoosePlaylistDialog(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
        ChoosePlaylistDialog(
            playlists = emptyList(),
            onAddToPlaylist = {},
            onAddNewPlaylist = {}
        )
    }
}
