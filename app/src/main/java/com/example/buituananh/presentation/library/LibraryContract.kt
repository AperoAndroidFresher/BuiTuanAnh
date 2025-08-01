package com.example.buituananh.presentation.library

import com.example.buituananh.model.Playlist
import com.example.buituananh.model.Song

data class LibraryState(
    val isLoading: Boolean = false,
    val localSongs: List<Song> = emptyList(),
    val isLocalSongs: Boolean = true,
    val remoteSongs: List<Song> = emptyList(),
    val playlistList: List<Playlist> = emptyList(),
    val isGrantedPermission: Boolean = false,
)

sealed interface LibraryIntent {
    data object LoadingPlaylistList : LibraryIntent
    data object LoadSongFiles : LibraryIntent
    data object LoadNetworkingSong : LibraryIntent
    data object ToggleLocalSong : LibraryIntent
    data class AddToPlayList(val song: Song) : LibraryIntent
    data class SharingSong(val song: Song) : LibraryIntent
    data class UpdatePermissionState(val isGranted: Boolean) : LibraryIntent
}


sealed interface LibraryEffect {
    data object NavigateToPlaylistScreen : LibraryEffect
    data class ShowToast(val message: String) : LibraryEffect
    data class SharingIntent(val song: Song) : LibraryEffect
}