package com.example.buituananh.data.remote.mapper

import com.example.buituananh.data.remote.model.response.artists.ArtistResponse
import com.example.buituananh.domain.model.Artist

fun ArtistResponse.toArtist(): Artist {
    return Artist(
        name = name,
        imageUrl = this.image.lastOrNull()?.text ?: ""
    )
}
