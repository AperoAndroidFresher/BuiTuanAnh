package com.example.buituananh.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Album(
     val name: String,
     val artistName: String,
     val imageUrl: String
)
