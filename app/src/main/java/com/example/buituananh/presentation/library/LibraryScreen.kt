@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.buituananh.presentation.library

import android.Manifest
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.presentation.library.item.ChoosePlaylistDialog
import com.example.buituananh.presentation.library.item.PermissionModal
import com.example.buituananh.presentation.library.item.SongItem
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Destination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

@Composable
fun LibraryScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel,
    onNavigate: (Destination) -> Unit
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    var permission = Manifest.permission.READ_EXTERNAL_STORAGE
    permission = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val mediaPermissionState = rememberPermissionState(permission)

    LaunchedEffect(Unit) {
        viewModel.channel.collect { effect ->
            when (effect) {
                LibraryEffect.NavigateToPlaylistScreen -> onNavigate(Destination.PlaylistWrapper)
                is LibraryEffect.SharingIntent -> {
                    val file = File(effect.song.filePath ?: "")
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "audio/*"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share audio"))
                }
                is LibraryEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LibraryScreen(
        modifier = modifier,
        state = state,
        permissionState = mediaPermissionState,
        onIntent = viewModel::onIntent
    )

}

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    state: LibraryState,
    permissionState: PermissionState? = null,
    onIntent: (LibraryIntent) -> Unit
) {

    val isGranted = permissionState?.status?.isGranted ?: false
    var acceptedByUser by remember {
        mutableStateOf(false)
    }
    var showModalPermission by remember {
        mutableStateOf(false)
    }

    var showPlaylistDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(acceptedByUser) {
        if(acceptedByUser) {
            permissionState?.launchPermissionRequest()
        }
    }

    LaunchedEffect(isGranted) {
        if(!isGranted) {
            showModalPermission = true
        } else {
            onIntent(LibraryIntent.LoadSongFiles)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Library",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            onIntent(LibraryIntent.ToggleLocalSong)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isLocalSongs) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        modifier = Modifier.width(130.dp)
                    ) {
                        Text(
                            text = "Local",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = if (state.isLocalSongs) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Button(
                        onClick = {
                            onIntent(LibraryIntent.ToggleLocalSong)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!state.isLocalSongs) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        modifier = Modifier.width(130.dp)
                    ) {
                        Text(
                            text = "Remote",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = if (!state.isLocalSongs) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
            if (state.isLocalSongs) {
                items(state.localSongs) { song ->
                   SongItem(
                       song = song,
                       onAddToPlaylistClick = {
                           onIntent(LibraryIntent.AddToPlayListClick(song))
                           showPlaylistDialog = true
                       }
                   ) {
                        onIntent(LibraryIntent.SharingSong(song))
                   }
                }
            }
        }
        if(showPlaylistDialog) {
            Dialog(
                onDismissRequest = {
                    showPlaylistDialog = false
                }
            ) {
                ChoosePlaylistDialog(
                    playlists = state.playlistList,
                    onAddNewPlaylist = {
                        onIntent(LibraryIntent.OnAddNewPlaylistClick)
                    }
                ) { playlist ->
                    onIntent(LibraryIntent.ChoosePlaylistToAdd(playlist))
                }
            }
        }
        if(showModalPermission) {
            Dialog(
                onDismissRequest = {
                    showModalPermission = false
                }
            ) {
                PermissionModal(
                    isDenied = {
                        showModalPermission = false
                    }
                ) {
                    acceptedByUser = true
                    showModalPermission = false
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewLibrary(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        LibraryScreen(state = LibraryState()) {

        }
    }

}