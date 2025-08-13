package com.example.buituananh.data.remote.model.response.album

import com.google.gson.annotations.SerializedName

data class TopAlbums(
    @SerializedName("album")
    val albumList: List<AlbumResponse>,

    @SerializedName("@attr")
    val attr: TopAlbumsAttr
)
