package com.example.buituananh.presentation.playlist

import com.example.buituananh.model.Playlist
import com.example.buituananh.model.Song
import com.example.buituananh.presentation.library.LibraryEffect

data class PlaylistState(
    val isLoading: Boolean = false,
    //detail playlist state
    val chosenPlaylist: Playlist? = null,
    val isGridMode: Boolean = false,
    val isSortMode: Boolean = false,
    val chosenSong: Song? = null,
    val backingPlaylist: Playlist? = null,

    //playlist state
    val playlistList: List<Playlist> = emptyList()
)

sealed interface PlaylistIntent {
    //playlist intent
    data object LoadPlaylist : PlaylistIntent
    data class CreateAPlaylist(val name: String) : PlaylistIntent
    data class RemoveAPlaylist(val playlist: Playlist) : PlaylistIntent
    data class RenamePlaylist(val name: String, val playlist: Playlist) : PlaylistIntent
    data class OnPlaylistClick(val id: Long) : PlaylistIntent

    //detail playlist intent
    data class LoadPlaylistById(val id: Long) : PlaylistIntent
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
    data class NavigateToDetailPlaylist(val id: Long) : PlaylistEffect
    data class SharingIntent(val song: Song) : PlaylistEffect
}
