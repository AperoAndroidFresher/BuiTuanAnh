package com.example.buituananh.data.local.mapper

import com.example.buituananh.data.local.model.SongEntity
import com.example.buituananh.domain.model.Song
import com.example.buituananh.util.SongSource
import com.example.buituananh.util.toPairDuration

fun SongEntity.toSong(): Song {
    return Song(
        songId = songId,
        title = title,
        artist = artist,
        duration = duration?.toPairDuration(),
        filePath = data,
        imageUri = imageUri,
        songSource = songSource
    )
}

fun Song.toEntity(): SongEntity {
    return SongEntity(
        songId = songId,
        title = title,
        artist = artist,
        duration = duration?.let { (minutes, seconds) ->
            (minutes * 60 + seconds) * 1000L
        },
        data = filePath,
        imageUri = imageUri,
        songSource = songSource
    )
}
