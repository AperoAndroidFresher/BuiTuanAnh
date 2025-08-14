package com.example.buituananh.util

import androidx.navigation3.runtime.NavKey
import com.example.buituananh.domain.model.Album
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

sealed interface Destination : NavKey {
    @Serializable
    data object SplashScreen : Destination

    @Serializable
    data object LoginScreen : Destination

    @Serializable
    data object SignupScreen : Destination

    @Serializable
    data object HomeWrapper : Destination

    @Serializable
    data object LibraryScreen : Destination

    @Serializable
    data object PlaylistScreen : Destination

    @Serializable
    data object ProfileScreen : Destination

    @Serializable
    data object PlaylistWrapper : Destination

    @Serializable
    data class DetailPlaylistScreen(val id: Long) : Destination

    @Serializable
    data object AuthWrapper : Destination
    
    @Serializable
    data object PlayerWrapper : Destination
    
    @Serializable
    data object SettingScreen : Destination
    
    @Serializable
    data class AlbumsScreen(val albums: List<Album>) : Destination

    @Serializable
    data class TrackScreen(val tracks: List<Track>) : Destination
    
    @Serializable
    data class ArtistScreen(val artists: List<Artist>) : Destination
    
    @Serializable
    data object HomeScreen : Destination
}
