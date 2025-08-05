package com.example.buituananh.domain.model

import android.net.Uri

data class Song(
    val songId: Long,
    val title: String?,
    val artist: String?,
    val duration: Pair<Int, Int>?,
    val filePath: String?,
    val imageUri: Uri?
)

