package com.example.buituananh.domain.model

import android.net.Uri

data class User(
    val userId: Long = 0,
    val username: String,
    val password: String,
    val email: String,
    val fullName: String? = null,
    val universityName: String? = null,
    val phoneNumber: String? = null,
    val description: String? = null,
    val avatarUri: Uri? = null
)
