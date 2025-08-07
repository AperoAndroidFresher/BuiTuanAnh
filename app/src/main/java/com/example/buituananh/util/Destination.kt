package com.example.buituananh.util

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destination : NavKey {
    @Serializable
    data object SplashScreen : Destination

    @Serializable
    data object LoginScreen : Destination

    @Serializable
    data object SignupScreen : Destination

    @Serializable
    data object HomeScreen : Destination

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
}
