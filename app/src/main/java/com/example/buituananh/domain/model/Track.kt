package com.example.buituananh.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val name: String,
    val playCount: String,
    val artistName: String,
    val imageUrl: String
)
