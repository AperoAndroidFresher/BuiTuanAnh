package com.example.buituananh.data.remote.model.response.artists

import com.example.buituananh.data.remote.model.response.album.Image

data class ArtistResponse(
    val image: List<Image>,
    val listeners: String,
    val mbid: String,
    val name: String,
    val playcount: String,
    val streamable: String,
    val url: String
)
