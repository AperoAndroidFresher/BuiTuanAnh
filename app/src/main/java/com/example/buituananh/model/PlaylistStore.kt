package com.example.buituananh.model

object PlaylistStore {

    val playlists = mutableListOf<Playlist>()

    fun addSongToPlaylist(song: Song?, playlist: Playlist): Boolean {
        if (song == null) return false
        val found = playlists.find { it == playlist }
        if (found == null) return false
        found.songs.add(song)
        return true
    }
}