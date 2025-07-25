package com.example.buituananh.presentation.playlist

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.buituananh.model.Song
import com.example.buituananh.model.listSongs
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier
) {

    val listSongsState = remember {
        mutableStateListOf<Song>().apply { addAll(listSongs) }
    }
    var backingUpList by remember {
        mutableStateOf<List<Song>>(emptyList())
    }

    val state = rememberReorderableLazyGridState(
        onMove = { from, to ->
            listSongsState.add(to.index, listSongsState.removeAt(from.index))
        }
    )

    var isGridMode by remember {
        mutableStateOf(false)
    }

    var isSortMode by remember {
        mutableStateOf(false)
    }

    var showPopup by remember {
        mutableStateOf(false)
    }

    var currentOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    var currentSong: Song? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current
    val displayMetrics = remember {
        context.resources.displayMetrics
    }
    val screenWidthPx = displayMetrics.widthPixels
    val popupWidthPx = with(LocalDensity.current) { 250.dp.toPx() }

    val safeOffsetX = if (currentOffset.x + popupWidthPx > screenWidthPx) {
        currentOffset.x - popupWidthPx
    } else {
        currentOffset.x - with(LocalDensity.current) { 50.dp.toPx() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        showPopup = false
                    })
            }) {

        Column {
            Spacer(Modifier.height(12.dp))

            HeaderSection(
                isGridMode = isGridMode,
                isSortMode = isSortMode,
                onSwitchToSortMode = {
                    isSortMode = true
                    backingUpList = listSongsState.toList()
                },
                onCancelSort = {
                    isSortMode = false
                    listSongsState.clear()
                    listSongsState.addAll(backingUpList)
                },
                onAcceptSort = {
                    isSortMode = false
                }
            ) { isGridMode = !isGridMode }

            Spacer(Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isGridMode) 2 else 1),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = state.gridState,
                modifier = Modifier
                    .fillMaxWidth()
                    .reorderable(state)
            ) {
                items(listSongsState) { song: Song ->
                    if (!isGridMode) {
                        ReorderableItem(
                            reorderableState = state,
                            key = song.id,
                        ) { isDragging ->
                            Log.d("A1", "${song.id}: $isDragging")
                            LinearSongItem(
                                modifier = Modifier
                                    .then(
                                        if (isSortMode) {
                                            Modifier
                                                .detectReorderAfterLongPress(state)
                                                .graphicsLayer {
                                                    alpha = if (isDragging) 0.9f else 1f
                                                    scaleX = if (isDragging) 1.2f else 1f
                                                    scaleY = if (isDragging) 1.2f else 1f
                                                }
                                        } else {
                                            Modifier
                                        }
                                    ),
                                isSortMode = isSortMode,
                                song = song
                            ) { (offset, song) ->
                                currentSong = song
                                if (currentOffset != offset) {
                                    currentOffset = offset
                                    showPopup = true
                                } else {
                                    showPopup = false
                                }
                            }
                        }
                    } else {
                        GridSongItem(
                            song = song
                        ) { (offset, song) ->
                            currentSong = song
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
                listSongsState.remove(currentSong)
                currentSong = null
            }) {
                //sharing feature
            }
        }

    }

}


@Preview(showSystemUi = true)
@Composable
fun PreviewLinearPlaylistScreen(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        PlaylistScreen()
    }

}