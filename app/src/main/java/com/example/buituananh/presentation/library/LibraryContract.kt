package com.example.buituananh.presentation.library

import com.example.buituananh.model.Playlist
import com.example.buituananh.model.Song

data class LibraryState(
    val isLoading: Boolean = false,
    val localSongs: List<Song> = emptyList(),
    val isLocalSongs: Boolean = true,
    val remoteSongs: List<Song> = emptyList(),
    val playlistList: List<Playlist> = emptyList(),
    val isGrantedPermission: Boolean
)

sealed interface LibraryIntent {
    data object LoadSongFiles : LibraryIntent
    data object LoadNetworkingSong : LibraryIntent
    data object ToggleLocalSong : LibraryIntent
    data object AddToPlayList : LibraryIntent
    data object SharingSong : LibraryIntent
    data class UpdatePermissionState(val isGranted: Boolean) : LibraryIntent
    data class SongPopupClick(val song: Song) : LibraryIntent
}
