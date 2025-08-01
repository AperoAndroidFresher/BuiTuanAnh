package com.example.buituananh.model

data class Playlist(
    val id: Int,
    val title: String,
    val songs: MutableList<Song>,
    val createTime: Long
) {
    fun addSong(song: Song) {
        songs.add(song)
    }

    fun removeSong(song: Song) {
        songs.remove(song)
    }
}
