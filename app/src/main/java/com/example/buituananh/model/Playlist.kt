package com.example.buituananh.model

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Playlist(
    val id: Long = System.currentTimeMillis(),
    var title: String,
    val songs: List<Song> = emptyList(),
    val createTime: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val imageBitmap: Bitmap? = null
)