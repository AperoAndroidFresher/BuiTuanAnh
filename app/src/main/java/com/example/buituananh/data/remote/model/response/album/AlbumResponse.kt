package com.example.buituananh.data.remote.model.response.album

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("playcount")
    val playCount: Int,

    @SerializedName("mbid")
    val mbid: String? = null,

    @SerializedName("url")
    val url: String,

    @SerializedName("artist")
    val artist: Artist,

    @SerializedName("image")
    val imageList: List<Image>,

    @SerializedName("@attr")
    val attr: AlbumAttr
)
