package com.example.buituananh.presentation.playlist.playlist_component

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.presentation.playlist.PlaylistEffect
import com.example.buituananh.presentation.playlist.PlaylistIntent
import com.example.buituananh.presentation.playlist.PlaylistState
import com.example.buituananh.presentation.playlist.PlaylistViewModel
import com.example.buituananh.presentation.playlist.detail_playlist_component.EmptyPlaylistNoti
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
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PlaylistEffect.NavigateToDetailPlaylist -> onNavigate(
                    Destination.DetailPlaylistScreen(
                        effect.id
                    )
                )

                is PlaylistEffect.ShareSongIntent -> {
                    val file = File(effect.song.filePath ?: "")
                    val uri =
                        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "audio/*"
                        putExtra(Intent.EXTRA_STREAM, effect.song.filePath)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(
                        Intent.createChooser(intent, "Share audio")
                    )
                }

                is PlaylistEffect.ShowDeleteSnackBar -> {
                    val result = snackBarHostState.showSnackbar(
                        effect.message,
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Long
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onIntent(PlaylistIntent.UndoRemovePlaylist)
                    }
                }

                is PlaylistEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    PlaylistScreen(
        modifier = modifier,
        state = state,
        snackbarState = snackBarHostState,
        onIntent = viewModel::onIntent
    )

}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    state: PlaylistState,
    snackbarState: SnackbarHostState,
    onIntent: (PlaylistIntent) -> Unit
) {

    LaunchedEffect(Unit) {
        onIntent(PlaylistIntent.LoadPlaylist)
        Log.d("PL1", "loading")
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
        snackbarHost = {
            SnackbarHost(snackbarState)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        if(state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (state.playlists.isEmpty()) {
                    item {
                        EmptyPlaylistNoti {
                            showCreationDialog = true
                        }
                    }
                } else {
                    items(state.playlists) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            removePlaylist = {
                                onIntent(PlaylistIntent.RemovePlaylist(playlist))
                            },
                            renamePlaylist = {
                                showRenameDialog = true
                                chosenPlaylist = playlist
                            },
                            modifier = Modifier.clickable {
                                onIntent(PlaylistIntent.SelectPlaylist(id = playlist.playlistId))
                            }
                        )
                    }
                }
            }
        }
        if (showCreationDialog) {
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
                    onIntent(PlaylistIntent.CreatePlaylist(it))
                }
            }
        }
        if (showRenameDialog) {
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
                    onIntent(
                        PlaylistIntent.RenamePlaylist(
                            name = newName,
                            playlist = chosenPlaylist ?: Playlist(title = "")
                        )
                    )
                }
            }
        }
    }

}
