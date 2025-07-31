package com.example.buituananh.presentation.playlist

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.model.Song
import com.example.buituananh.presentation.playlist.item.CustomPopupSong
import com.example.buituananh.presentation.playlist.item.GridSongItem
import com.example.buituananh.presentation.playlist.item.HeaderSection
import com.example.buituananh.presentation.playlist.item.LinearSongItem
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun PlaylistScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value

    var permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_AUDIO
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val mediaPermissionState = rememberPermissionState(permission) { isGranted ->
        if(isGranted) {

        } else {

        }
    }

    PlaylistScreen(
        state = state,
        permissionState = mediaPermissionState,
        onIntent = viewModel::onIntent
    )

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    state: PlaylistState,
    permissionState: PermissionState,
    onIntent: (PlaylistIntent) -> Unit
) {

    var showPopup by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val displayMetrics = remember {
        context.resources.displayMetrics
    }
    var currentOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    val screenWidthPx = displayMetrics.widthPixels
    val popupWidthPx = with(LocalDensity.current) { 250.dp.toPx() }

    val safeOffsetX = if (currentOffset.x + popupWidthPx > screenWidthPx) {
        currentOffset.x - popupWidthPx
    } else {
        currentOffset.x - with(LocalDensity.current) { 50.dp.toPx() }
    }

    if(permissionState.status.isGranted) {

        LaunchedEffect(permissionState.status.isGranted) {
            onIntent(PlaylistIntent.LoadData)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onIntent(PlaylistIntent.ToggleSortMode(false))
                        })
                }) {

            Column {
                Spacer(Modifier.height(12.dp))

                HeaderSection(
                    isGridMode = state.isGridMode,
                    isSortMode = state.isSortMode,
                    onSwitchToSortMode = {
                        onIntent(PlaylistIntent.ToggleSortMode(true))
                    },
                    onCancelSort = {
                        onIntent(PlaylistIntent.CancelSortMode)
                    },
                    onAcceptSort = {
                        onIntent(PlaylistIntent.SaveSortMode)
                    }
                ) {
                    onIntent(PlaylistIntent.ToggleGridMode)
                }

                Spacer(Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(if (state.isGridMode) 2 else 1),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(state.playlist) { song: Song ->
                        if (!state.isGridMode) {
                            LinearSongItem(
                                modifier = Modifier.animateItem()
                                    .then(
                                        if (state.isGridMode) {
                                            Modifier
                                        } else {
                                            Modifier
                                        }
                                    ),
                                isSortMode = state.isSortMode,
                                song = song
                            ) { (offset, song) ->
                                onIntent(PlaylistIntent.SongPopupClick(song))
                                if (currentOffset != offset) {
                                    currentOffset = offset
                                    showPopup = true
                                } else {
                                    showPopup = false
                                }
                            }
                        } else {
                            GridSongItem(
                                song = song,
                                modifier = Modifier.animateItem()
                            ) { (offset, song) ->
                                onIntent(PlaylistIntent.SongPopupClick(song))
                                if (currentOffset != offset) {
                                    currentOffset = offset
                                    showPopup = true
                                } else {
                                    showPopup = false
                                }
                            }
                        }
                    }
                }
            }

            if (showPopup) {
                CustomPopupSong(modifier = Modifier.offset {
                    IntOffset(
                        safeOffsetX.roundToInt(), currentOffset.y.roundToInt()
                    )
                }, onRemove = {
                    showPopup = false
                    onIntent(PlaylistIntent.RemoveSongFromPlaylist)
                }) {
                    //sharing feature
                }
            }

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("The permission is needed to process the application.")
            Button(onClick = {
                permissionState.launchPermissionRequest()
            }) {
                Text("Request permission")
            }
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun PreviewLinearPlaylistScreen(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {

    }

}