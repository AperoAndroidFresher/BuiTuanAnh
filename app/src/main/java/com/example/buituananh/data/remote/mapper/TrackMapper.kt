package com.example.buituananh.data.remote.mapper

import com.example.buituananh.data.remote.model.response.TrackResponse
import com.example.buituananh.domain.model.Track

fun TrackResponse.toTrack(): Track {
    return Track(
        name = name,
        playCount = playCount,
        artistName = artist.name,
        imageUrl = imageList.lastOrNull()?.text ?: ""
    )
}
