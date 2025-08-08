package com.example.buituananh.domain.model

import android.net.Uri
import android.os.Parcelable
import com.example.buituananh.util.SongSource
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Parcelize
data class Song(
    val songId: Long,
    val title: String?,
    val artist: String?,
    val duration: Pair<Int, Int>?,
    val filePath: String?,
    val imageUri: Uri?,
    val songSource: SongSource
) : Parcelable

