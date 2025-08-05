package com.example.buituananh.presentation.library

import android.content.Context
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.Song

data class LibraryState(
    val isLoading: Boolean = false,
    val localSongs: List<Song> = emptyList(),
    val isLocalSongs: Boolean = true,
    val remoteSongs: List<Song> = emptyList(),
    val playlistList: List<Playlist> = emptyList(),
    val chosenSong: Song? = null,
    val userId: Long = -1
)

sealed interface LibraryIntent {
    data class LoadSongFiles(val context: Context) : LibraryIntent
    data object LoadNetworkingSong : LibraryIntent
    data object ToggleLocalSong : LibraryIntent
    data class AddToPlayListClick(val song: Song) : LibraryIntent
    data class SharingSong(val song: Song) : LibraryIntent
    data object OnAddNewPlaylistClick : LibraryIntent
    data class ChoosePlaylistToAdd(val playlist: Playlist) : LibraryIntent
}


sealed interface LibraryEffect {
    data object NavigateToPlaylistScreen : LibraryEffect
    data class ShowToast(val message: String) : LibraryEffect
    data class SharingIntent(val song: Song) : LibraryEffect
}
