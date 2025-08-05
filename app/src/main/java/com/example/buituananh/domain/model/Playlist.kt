package com.example.buituananh.domain.model

import android.net.Uri
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Playlist(
    val playlistId: Long = System.currentTimeMillis(),
    var title: String,
    val songs: List<Song> = emptyList(),
    val createTime: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val imageUri: Uri? = null,
    val isDeleted: Boolean = false,
)