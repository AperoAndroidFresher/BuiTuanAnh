package com.example.buituananh.data.remote.mapper

import com.example.buituananh.data.remote.model.response.SongResponse
import com.example.buituananh.domain.model.Song
import com.example.buituananh.util.SongSource
import com.example.buituananh.util.toPairDuration

fun SongResponse.toSong(): Song 
    = Song(
        songId = System.currentTimeMillis(),
        title = title,
        artist = artist,
        duration = duration.toLong().toPairDuration(),
        filePath = path,
        imageUri = null,
        songSource = SongSource.REMOTE
    )
