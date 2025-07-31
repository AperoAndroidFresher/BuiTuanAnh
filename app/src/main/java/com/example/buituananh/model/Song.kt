package com.example.buituananh.model

import android.graphics.Bitmap

data class Song(
    val id: Long,
    val title: String?,
    val artist: String?,
    val duration: Pair<Int, Int>?,
    val filePath: String?,
    val image: Bitmap?
)

