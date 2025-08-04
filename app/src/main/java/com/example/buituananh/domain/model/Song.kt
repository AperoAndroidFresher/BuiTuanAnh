package com.example.buituananh.domain.model

import android.net.Uri

data class Song(
    val id: Long,
    val title: String?,
    val artist: String?,
    val duration: Pair<Int, Int>?,
    val filePath: String?,
    val image: Uri?
)

