package com.example.buituananh.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "musics",
    indices = [
        Index(value = ["data"], unique = true)
    ]
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val songId: Long = 0,
    val title: String?,
    val artist: String?,
    val duration: Long?,
    val data: String?,
    val imageUri: Uri?,
)
