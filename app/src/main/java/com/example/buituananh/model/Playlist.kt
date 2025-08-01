package com.example.buituananh.model

import android.graphics.Bitmap

data class Playlist(
    val id: Int,
    val title: String,
    val songs: MutableList<Song>,
    val createTime: Long,
    val imageBitmap: Bitmap? = null
) {
    fun addSong(song: Song) {
        songs.add(song)
    }

    fun removeSong(song: Song) {
        songs.remove(song)
    }
}
