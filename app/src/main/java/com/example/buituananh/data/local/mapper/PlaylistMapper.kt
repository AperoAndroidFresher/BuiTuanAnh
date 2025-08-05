package com.example.buituananh.data.local.mapper

import com.example.buituananh.data.local.model.PlaylistEntity
import com.example.buituananh.data.local.model.PlaylistWithSongs
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

fun PlaylistWithSongs.toPlaylist(): Playlist {
    return Playlist(
        playlistId = this.playlist.playlistId,
        title = this.playlist.title,
        createTime = this.playlist.createTime,
        imageUri = this.playlist.imageUri,
        isDeleted = this.playlist.isDeleted,
        songs = this.song.map { it.toSong() }
    )
}
