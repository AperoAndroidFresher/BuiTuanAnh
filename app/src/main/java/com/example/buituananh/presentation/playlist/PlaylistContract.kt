package com.example.buituananh.presentation.playlist

import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.Song

data class PlaylistState(
    val isLoading: Boolean = false,
    val isGridMode: Boolean = false,
    val isSortMode: Boolean = false,
    
    val selectedPlaylist: Playlist? = null,
    val selectedSong: Song? = null,
    val backingPlaylist: Playlist? = null,
    
    val playlists: List<Playlist> = emptyList(),
    val userId: Long = -1,
    val deletedPlaylistId: Long = -1
)

sealed interface PlaylistIntent {
    //playlist intent
    data object LoadPlaylist : PlaylistIntent
    data class CreatePlaylist(val name: String) : PlaylistIntent
    data class RemovePlaylist(val playlist: Playlist) : PlaylistIntent
    data class RenamePlaylist(val name: String, val playlist: Playlist) : PlaylistIntent
    data class SelectPlaylist(val id: Long) : PlaylistIntent
    data object UndoRemovePlaylist : PlaylistIntent

    //detail playlist intent
    data class LoadPlaylistDetail(val id: Long) : PlaylistIntent
    data object ToggleGridMode : PlaylistIntent
    data object RemoveSong : PlaylistIntent
    data object ShareSong : PlaylistIntent
    data class ClickSongPopup(val song: Song) : PlaylistIntent
    data class ToggleSortMode(val currentSortMode: Boolean) : PlaylistIntent
    data object CancelSortMode : PlaylistIntent
    data object SaveSortMode : PlaylistIntent
    data class DragSong(val fromIndex: Int, val toIndex: Int) : PlaylistIntent
}

sealed interface PlaylistEffect {
    data class NavigateToDetailPlaylist(val id: Long) : PlaylistEffect
    data class ShareSongIntent(val song: Song) : PlaylistEffect
    data class ShowDeleteSnackBar(val message: String) : PlaylistEffect
    data class ShowToast(val message: String) : PlaylistEffect
}
