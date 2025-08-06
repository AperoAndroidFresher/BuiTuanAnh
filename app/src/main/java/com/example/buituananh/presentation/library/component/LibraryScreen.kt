@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.buituananh.presentation.library.component

import android.Manifest
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.presentation.components.LoadingAnimation
import com.example.buituananh.presentation.components.TopBar
import com.example.buituananh.presentation.library.LibraryEffect
import com.example.buituananh.presentation.library.LibraryIntent
import com.example.buituananh.presentation.library.LibraryState
import com.example.buituananh.presentation.library.LibraryViewModel
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
    onNavigate: (Destination) -> Unit,
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
                is LibraryEffect.ShareSongIntent -> {
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
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun LibraryScreen(
    state: LibraryState,
    onIntent: (LibraryIntent) -> Unit,
    modifier: Modifier = Modifier,
    permissionState: PermissionState? = null,
) {

    val isGranted = permissionState?.status?.isGranted ?: false
    val context = LocalContext.current

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
        if (acceptedByUser) {
            permissionState?.launchPermissionRequest()
        }
    }

    LaunchedEffect(isGranted) {
        if (!isGranted) {
            showModalPermission = true
        } else {
            if (state.localSongs.isEmpty()) {
                onIntent(LibraryIntent.LoadLocalSongs(context = context))
            }
        }
    }

    LaunchedEffect(state.isLocalMode) {
        if (!state.isLocalMode && state.remoteSongs.isEmpty()) {
            onIntent(LibraryIntent.LoadNetworkSongs)
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Library")
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            ButtonSection(state = state, onIntent = onIntent, modifier = Modifier)
            if (state.isLoading) {
                LoadingAnimation()
            } else if (state.networkError != null) {
                NoInternetSection(
                    fetchSongAgain = {
                        onIntent(LibraryIntent.LoadNetworkSongs)
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                SongsSection(
                    clickSongOptions = {
                        onIntent(LibraryIntent.ClickSongOptions(it))
                        showPlaylistDialog = true
                    },
                    shareSong = {
                        onIntent(LibraryIntent.ShareSong(it))
                    },
                    modifier = Modifier,
                    isLocalMode = state.isLocalMode,
                    localSongs = state.localSongs,
                    remoteSongs = state.remoteSongs,
                )
            }
        }

        if (showPlaylistDialog) {
            Dialog(
                onDismissRequest = { showPlaylistDialog = false },
            ) {
                ChoosePlaylistDialog(
                    playlists = state.playlists,
                    onAddNewPlaylist = { onIntent(LibraryIntent.ClickNewPlaylist) },
                ) { playlist ->
                    onIntent(LibraryIntent.ClickPlaylist(playlist))
                }
            }
        }
        if (showModalPermission) {
            Dialog(
                onDismissRequest = { showModalPermission = false },
            ) {
                PermissionModal(
                    isDenied = { showModalPermission = false },
                ) {
                    acceptedByUser = true
                    showModalPermission = false
                }
            }
        }
    }
}

@Composable
private fun SongsSection(
    clickSongOptions: (Song) -> Unit,
    shareSong: (Song) -> Unit,
    modifier: Modifier = Modifier,
    isLocalMode: Boolean = true,
    localSongs: List<Song> = emptyList(),
    remoteSongs: List<Song> = emptyList(),
) {

    val songs = if (isLocalMode) {
        localSongs
    } else {
        remoteSongs
    }

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(songs) { song ->
            SongItem(
                song = song,
                clickSongOptions = {
                    clickSongOptions(song)
                },
                shareSong = {
                    shareSong(song)
                },
            )
        }
    }
}

@Composable
private fun ButtonSection(
    state: LibraryState,
    onIntent: (LibraryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier.height(24.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        LibraryModeButton(
            onClick = {
                onIntent(LibraryIntent.ToggleLocalMode)
            },
            isLocalMode = state.isLocalMode,
            title = "Local",
        )
        LibraryModeButton(
            onClick = {
                onIntent(LibraryIntent.ToggleLocalMode)
            },
            isLocalMode = !state.isLocalMode,
            title = "Remote",
        )
    }
}

@Composable
private fun LibraryModeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLocalMode: Boolean = false,
    title: String = "",
) {
    val containerColor = if (isLocalMode) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val textColor = if (isLocalMode) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
        ),
        modifier = modifier.width(130.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = textColor,
        )
    }
}

@Composable
private fun NoInternetSection(
    fetchSongAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.no_internet),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(100.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = """
                No internet connection,
                please check your
                connection again
            """.trimIndent(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = fetchSongAgain,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                text = "Try again",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLibrary(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        LibraryScreen(state = LibraryState(), onIntent = {})
    }
}
