package com.example.buituananh.data.remote.mapper

import com.example.buituananh.data.remote.model.response.album.AlbumResponse
import com.example.buituananh.domain.model.Album

fun AlbumResponse.toAlbum(): Album {
    return Album(
        name = name,
        artistName = artist.name,
        imageUrl = imageList.lastOrNull()?.text ?: ""
    )
}
