package com.example.buituananh.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class TopTracksResponse(
    @SerializedName("toptracks")
    val topTracks: TopTracks
)

data class TopTracks(
    @SerializedName("track")
    val trackList: List<TrackResponse>,

    @SerializedName("@attr")
    val attr: TopTracksAttr
)

data class TrackResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("duration")
    val duration: String,

    @SerializedName("playcount")
    val playCount: String,

    @SerializedName("listeners")
    val listeners: String,

    @SerializedName("mbid")
    val mbid: String? = null,

    @SerializedName("url")
    val url: String,

    @SerializedName("streamable")
    val streamable: String,

    @SerializedName("artist")
    val artist: ArtistTrack,

    @SerializedName("image")
    val imageList: List<Image>
)

data class Streamable(
    @SerializedName("#text")
    val text: String,

    @SerializedName("fulltrack")
    val fullTrack: String
)

data class ArtistTrack(
    @SerializedName("name")
    val name: String,

    @SerializedName("mbid")
    val mbid: String? = null,

    @SerializedName("url")
    val url: String
)

data class Image(
    @SerializedName("#text")
    val text: String,

    @SerializedName("size")
    val size: String
)

data class TopTracksAttr(
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
