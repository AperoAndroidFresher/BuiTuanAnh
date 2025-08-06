@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.buituananh.presentation.library.component

import android.Manifest
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.R
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
    modifier: Modifier = Modifier,
    state: LibraryState,
    permissionState: PermissionState? = null,
    onIntent: (LibraryIntent) -> Unit,
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
            onIntent(LibraryIntent.LoadLocalSongs(context = context))
        }
    }

    LaunchedEffect(state.isLocalMode) {
        if (!state.isLocalMode) {
            onIntent(LibraryIntent.LoadNetworkSongs)
            Log.d("S1", "LibraryScreen: Loading")
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Library",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(
                        onClick = {
                            onIntent(LibraryIntent.ToggleLocalMode)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isLocalMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        modifier = Modifier.width(130.dp),
                    ) {
                        Text(
                            text = "Local",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = if (state.isLocalMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Button(
                        onClick = {
                            onIntent(LibraryIntent.ToggleLocalMode)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!state.isLocalMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        modifier = Modifier.width(130.dp),
                    ) {
                        Text(
                            text = "Remote",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = if (!state.isLocalMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
            if (state.isLoading) {
                item {
                    CircularProgressIndicator()
                }
            } else if(state.networkError != null) {
                item { 
                    NoInternetSection(modifier = Modifier.fillMaxSize()) { 
                        onIntent(LibraryIntent.LoadNetworkSongs)
                    }
                }
            } else {
                if (state.isLocalMode) {
                    items(state.localSongs) { song ->
                        SongItem(
                            song = song,
                            onAddToPlaylistClick = {
                                onIntent(LibraryIntent.ClickSongOptions(song))
                                showPlaylistDialog = true
                            },
                        ) {
                            onIntent(LibraryIntent.ShareSong(song))
                        }
                    }
                } else {
                    items(state.remoteSongs) { song ->
                        SongItem(
                            song = song,
                            onAddToPlaylistClick = {
                                onIntent(LibraryIntent.ClickSongOptions(song))
                                showPlaylistDialog = true
                            },
                        ) {

                        }
                    }
                }
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
fun NoInternetSection(
    modifier: Modifier = Modifier,
    fetchSongAgain: () -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.no_internet),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.onSurface),
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
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = fetchSongAgain,
            shape = MaterialTheme.shapes.large
        ) { 
            Text(
                text = "Try again",
                style = MaterialTheme.typography.bodyMedium
            )
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
