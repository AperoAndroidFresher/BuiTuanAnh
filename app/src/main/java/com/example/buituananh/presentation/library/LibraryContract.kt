package com.example.buituananh.presentation.library

import android.content.Context
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.Song

data class LibraryState(
    val isLoading: Boolean = false,
    val networkError: String? = null,
    val localSongs: List<Song> = emptyList(),
    val isLocalMode: Boolean = true,
    val remoteSongs: List<Song> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val selectedSong: Song? = null,
    val userId: Long = -1,
    val playedSong: Song? = null
)

sealed interface LibraryIntent {
    data class LoadLocalSongs(val context: Context) : LibraryIntent
    data object LoadNetworkSongs : LibraryIntent
    data object ToggleLocalMode : LibraryIntent
    data class ClickSongOptions(val song: Song) : LibraryIntent
    data class ShareSong(val song: Song) : LibraryIntent
    data object ClickNewPlaylist : LibraryIntent
    data class ClickPlaylist(val playlist: Playlist) : LibraryIntent
    data class StartSong(val song: Song) : LibraryIntent
}


sealed interface LibraryEffect {
    data object NavigateToPlaylistScreen : LibraryEffect
    data class ShowToast(val message: String) : LibraryEffect
    data class ShareSongIntent(val song: Song) : LibraryEffect
}
