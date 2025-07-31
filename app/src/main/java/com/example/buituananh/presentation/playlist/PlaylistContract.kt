package com.example.buituananh.presentation.playlist

import com.example.buituananh.model.Song

data class PlaylistState(
    val isLoading: Boolean = false,
    val playlist: List<Song> = emptyList(),
    val isGridMode: Boolean = false,
    val isSortMode: Boolean = false,
    val chosenSong: Song? = null,
    val backingPlaylist: List<Song>? = null
)

sealed interface PlaylistIntent {
    data object LoadData : PlaylistIntent
    data object ToggleGridMode : PlaylistIntent
    data object RemoveSongFromPlaylist : PlaylistIntent
    data object SharingSong : PlaylistIntent
    data class SongPopupClick(val song: Song) : PlaylistIntent
    data class ToggleSortMode(val currentSortMode: Boolean) : PlaylistIntent
    data object CancelSortMode : PlaylistIntent
    data object SaveSortMode : PlaylistIntent
    data class OnDragging(val fromIndex: Int, val toIndex: Int) : PlaylistIntent
}

sealed interface PlaylistEffect {
    //Ongoing
}
