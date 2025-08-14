package com.example.buituananh.data.remote.model.response

import com.example.buituananh.data.remote.model.response.album.TopAlbums
import com.google.gson.annotations.SerializedName

data class TopAlbumsResponse(
    @SerializedName("topalbums")
    val topAlbums: TopAlbums
)
