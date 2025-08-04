package com.example.buituananh.data.mapper

import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.domain.model.Playlist

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        playlistId = playlistId,
        title = title,
        createTime = createTime,
        imageUri = imageUri,
        isDeleted = isDeleted
    )
}

fun Playlist.toEntity(ownerId: Long): PlaylistEntity {
    return PlaylistEntity(
        playlistId = playlistId,
        title = title,
        createTime = createTime,
        imageUri = imageUri,
        isDeleted = isDeleted,
        ownerId =  ownerId
    )
}