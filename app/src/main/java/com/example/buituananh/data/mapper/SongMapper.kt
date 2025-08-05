package com.example.buituananh.data.mapper

import com.example.buituananh.data.model.SongEntity
import com.example.buituananh.domain.model.Song
import com.example.buituananh.util.toPairDuration

fun SongEntity.toSong(): Song {
    return Song(
        id = songId,
        title = title,
        artist = artist,
        duration = duration?.toPairDuration(),
        filePath = data,
        image = imageUri
    )
}

fun Song.toEntity(): SongEntity {
    return SongEntity(
        songId = id,
        title = title,
        artist = artist,
        duration = duration?.let { (minutes, seconds) ->
            (minutes * 60 + seconds) * 1000L
        },
        data = filePath,
        imageUri = image
    )
}
