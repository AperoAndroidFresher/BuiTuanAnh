package com.example.buituananh.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val name: String,
    val imageUrl: String
)
