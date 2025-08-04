package com.example.buituananh.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.buituananh.domain.model.User

data class UserWithPlaylists(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "ownerId"
    )
    val playlists: List<PlaylistEntity>
)
