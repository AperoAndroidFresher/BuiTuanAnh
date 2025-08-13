package com.example.buituananh.presentation.home

import com.example.buituananh.domain.model.Album
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track
import com.example.buituananh.domain.model.User

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val albums: List<Album> = emptyList(),
    val tracks: List<Track> = emptyList(),
    val artists: List<Artist> = emptyList(),
)

sealed interface HomeIntent {
    data object LoadUserData : HomeIntent
    data object LoadAlbumTrackArtist : HomeIntent
    data object ClickSetting : HomeIntent
    data object ClickProfile : HomeIntent
    data object ClickSeeAllAlbums : HomeIntent
    data object ClickSeeAllArtists : HomeIntent
    data object ClickSeeAllTracks : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToSettingScreen : HomeEffect
    data object NavigateToProfileScreen : HomeEffect
    data class NavigateToAlbumsScreen(val albums: List<Album>) : HomeEffect
    data class NavigateToTracksScreen(val tracks: List<Track>) : HomeEffect
    data class NavigateToArtistScreen(val artist: List<Artist>) : HomeEffect
    data class ShowToast(val message: String) : HomeEffect
}

