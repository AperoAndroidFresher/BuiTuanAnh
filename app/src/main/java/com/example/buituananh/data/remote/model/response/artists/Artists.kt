package com.example.buituananh.data.remote.model.response.artists

import com.google.gson.annotations.SerializedName

data class Artists(
    @SerializedName("@attr")
    val attr: Attr,
    val artist: List<ArtistResponse>
)
