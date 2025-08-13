package com.example.buituananh.data.remote.model.response.album

import com.google.gson.annotations.SerializedName

data class TopAlbumsAttr(
    @SerializedName("artist")
    val artist: String,

    @SerializedName("page")
    val page: String,

    @SerializedName("perPage")
    val perPage: String,

    @SerializedName("totalPages")
    val totalPages: String,

    @SerializedName("total")
    val total: String
)
