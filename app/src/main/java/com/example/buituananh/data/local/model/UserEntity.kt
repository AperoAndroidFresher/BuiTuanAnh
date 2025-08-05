package com.example.buituananh.data.local.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["username"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val username: String,
    val password: String,
    val email: String,
    val fullName: String?,
    val universityName: String?,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String?,
    val description: String?,
    @ColumnInfo(name = "avatar_uri")
    val avatarUri: Uri?
)
