package com.example.buituananh.presentation.playlist

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.model.Playlist
import com.example.buituananh.presentation.playlist.detail_playlist_item.EmptyPlaylistNoti
import com.example.buituananh.presentation.playlist.playlist_item.NewPlaylistDialog
import com.example.buituananh.presentation.playlist.playlist_item.PlaylistItem
import com.example.buituananh.presentation.playlist.playlist_item.RenamePlaylistDialog
import com.example.buituananh.util.Destination
import java.io.File

@Composable
fun PlaylistScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
    onNavigate: (Destination) -> Unit
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                is PlaylistEffect.NavigateToDetailPlaylist -> onNavigate(Destination.DetailPlaylistScreen(effect.id))
                is PlaylistEffect.SharingIntent -> {
                    val file = File(effect.song.filePath ?: "")
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "audio/*"
                        putExtra(Intent.EXTRA_STREAM, effect.song.filePath)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(
                        Intent.createChooser(intent, "Share audio")
                    )
                }
            }
        }
    }

    PlaylistScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::onIntent
    )

}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    state: PlaylistState,
    onIntent: (PlaylistIntent) -> Unit
) {

    LaunchedEffect(Unit) {
        onIntent(PlaylistIntent.LoadPlaylist)
    }

    var showCreationDialog by remember {
        mutableStateOf(false)
    }

    var showRenameDialog by remember {
        mutableStateOf(false)
    }

    var chosenPlaylist by remember {
        mutableStateOf<Playlist?>(null)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.size(30.dp))

                Text(
                    text = "My Playlist",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = {
                        showCreationDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (state.playlistList.isEmpty()) {
                item {
                    EmptyPlaylistNoti {
                        showCreationDialog = true
                    }
                }
            } else {
                items(state.playlistList) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        removePlaylist = {
                            onIntent(PlaylistIntent.RemoveAPlaylist(playlist))
                        },
                        renamePlaylist = {
                            showRenameDialog = true
                            chosenPlaylist = playlist
                        },
                        modifier = Modifier.clickable {
                            onIntent(PlaylistIntent.OnPlaylistClick(id = playlist.id))
                        }
                    )
                }
            }
        }
       if(showCreationDialog) {
           Dialog(
               onDismissRequest = {
                   showCreationDialog = false
               }
           ) {
               NewPlaylistDialog(
                   onCancel = {
                       showCreationDialog = false
                   }
               ) {
                    onIntent(PlaylistIntent.CreateAPlaylist(it))
               }
           }
       }
        if(showRenameDialog) {
            Dialog(
                onDismissRequest = {
                    showCreationDialog = false
                }
            ) {
                RenamePlaylistDialog(
                    title = chosenPlaylist?.title ?: "",
                    onCancel = {
                        showRenameDialog = false
                    }
                ) { newName ->
                    onIntent(PlaylistIntent.RenamePlaylist(
                        name = newName,
                        playlist = chosenPlaylist ?: Playlist(title = ""))
                    )
                }
            }
        }
    }

}