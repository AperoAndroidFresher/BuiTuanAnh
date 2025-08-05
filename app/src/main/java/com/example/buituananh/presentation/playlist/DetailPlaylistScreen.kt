package com.example.buituananh.presentation.playlist

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.domain.model.Song
import com.example.buituananh.presentation.playlist.detail_playlist_item.CustomPopupSong
import com.example.buituananh.presentation.playlist.detail_playlist_item.GridSongItem
import com.example.buituananh.presentation.playlist.detail_playlist_item.HeaderSection
import com.example.buituananh.presentation.playlist.detail_playlist_item.LinearSongItem
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Destination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.channels.Channel
import kotlin.math.roundToInt

@Composable
fun DetailPlaylistScreenRoot(
    modifier: Modifier = Modifier,
    id: Long,
    viewModel: PlaylistViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                is PlaylistEffect.NavigateToDetailPlaylist -> {

                }
                is PlaylistEffect.SharingIntent -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "audio/*"
                        putExtra(Intent.EXTRA_STREAM, effect.song.filePath)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(
                        Intent.createChooser(intent, "Share audio")
                    )
                }

                is PlaylistEffect.ShowSnackBar -> {

                }

                is PlaylistEffect.ShowToast -> {

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(PlaylistIntent.LoadPlaylistById(id))
    }

    DetailPlaylistScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::onIntent
    )

}

@Composable
fun DetailPlaylistScreen(
    modifier: Modifier = Modifier,
    state: PlaylistState,
    onIntent: (PlaylistIntent) -> Unit
) {

    var showPopup by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    //popup calculation
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



    //dragging calculation
    val stateList = rememberLazyListState()

    var draggingItemIndex: Int? by remember {
        mutableStateOf(null)
    }

    var delta: Float by remember {
        mutableFloatStateOf(0f)
    }

    var draggingItem: LazyListItemInfo? by remember {
        mutableStateOf(null)
    }

    val scrollChannel = Channel<Float>()

    LaunchedEffect(stateList) {
        while (true) {
            val diff = scrollChannel.receive()
            stateList.scrollBy(diff)
        }
    }


    Box(
        modifier = modifier
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
                title = state.chosenPlaylist?.title ?: "null",
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

            if (state.isGridMode) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val songList = state.chosenPlaylist?.songs
                    if(songList != null) {
                        items(state.chosenPlaylist.songs) { song: Song ->
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
                    } else {
                        item {
                            Text("NUll")
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = stateList,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .then(
                            if (state.isSortMode) {
                                Modifier
                                    .pointerInput(key1 = stateList) {
                                        detectDragGesturesAfterLongPress(
                                            onDragStart = { offset ->
                                                stateList.layoutInfo.visibleItemsInfo
                                                    .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                                                    ?.also {
                                                        (it.contentType as? DraggableItem)?.let { draggableItem ->
                                                            draggingItem = it
                                                            draggingItemIndex = draggableItem.index
                                                        }
                                                    }
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                delta += dragAmount.y

                                                val currentDraggingItemIndex =
                                                    draggingItemIndex
                                                        ?: return@detectDragGesturesAfterLongPress
                                                val currentDraggingItem =
                                                    draggingItem
                                                        ?: return@detectDragGesturesAfterLongPress

                                                val startOffset = currentDraggingItem.offset + delta
                                                val endOffset =
                                                    currentDraggingItem.offset + currentDraggingItem.size + delta
                                                val middleOffset =
                                                    startOffset + (endOffset - startOffset) / 2

                                                val targetItem =
                                                    stateList.layoutInfo.visibleItemsInfo.find { item ->
                                                        middleOffset.toInt() in item.offset..item.offset + item.size &&
                                                                currentDraggingItem.index != item.index &&
                                                                item.contentType is DraggableItem
                                                    }

                                                if (targetItem != null) {
                                                    val targetIndex =
                                                        (targetItem.contentType as DraggableItem).index
                                                    onIntent(
                                                        PlaylistIntent.OnDragging(
                                                            currentDraggingItemIndex,
                                                            targetIndex
                                                        )
                                                    )
                                                    draggingItemIndex = targetIndex
                                                    delta += currentDraggingItem.offset - targetItem.offset
                                                    draggingItem = targetItem
                                                } else {
                                                    val startOffsetToTop =
                                                        startOffset - stateList.layoutInfo.viewportStartOffset
                                                    val endOffsetToBottom =
                                                        endOffset - stateList.layoutInfo.viewportEndOffset
                                                    val scroll =
                                                        when {
                                                            startOffsetToTop < 0 -> startOffsetToTop.coerceAtMost(
                                                                0f
                                                            )

                                                            endOffsetToBottom > 0 -> endOffsetToBottom.coerceAtLeast(
                                                                0f
                                                            )

                                                            else -> 0f
                                                        }
                                                    val canScrollDown =
                                                        currentDraggingItemIndex != (state.chosenPlaylist?.songs?.size
                                                            ?: (1 - 1)) && endOffsetToBottom > 0
                                                    val canScrollUp =
                                                        currentDraggingItemIndex != 0 && startOffsetToTop < 0
                                                    if (scroll != 0f && (canScrollUp || canScrollDown)) {
                                                        scrollChannel.trySend(scroll)
                                                    }
                                                }
                                            },
                                            onDragEnd = {
                                                draggingItem = null
                                                draggingItemIndex = null
                                                delta = 0f
                                            },
                                            onDragCancel = {
                                                draggingItem = null
                                                draggingItemIndex = null
                                                delta = 0f
                                            },
                                        )
                                    }
                            } else {
                                Modifier
                            }
                        )
                ) {
                    itemsIndexed(
                        items = state.chosenPlaylist?.songs ?: emptyList(),
                        contentType = { index, song -> DraggableItem(index = index) }
                    ) { index, song ->
                        val linearModifier = if (draggingItemIndex == index) {
                            Modifier
                                .zIndex(1f)
                                .graphicsLayer {
                                    translationY = delta
                                }
                        } else {
                            Modifier.animateItem()
                        }
                        LinearSongItem(
                            song = song,
                            isSortMode = state.isSortMode,
                            modifier = linearModifier.animateItem()
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
                onIntent(PlaylistIntent.SharingSong)
            }
        }

    }

}

data class DraggableItem(val index: Int)

@Preview(showSystemUi = true)
@Composable
fun PreviewLinearPlaylistScreen(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {

    }

}