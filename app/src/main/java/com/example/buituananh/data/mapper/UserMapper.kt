package com.example.buituananh.data.mapper

import com.example.buituananh.data.model.UserEntity
import com.example.buituananh.domain.model.User

fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        username = this.username,
        password = this.password,
        email = this.email,
        fullName = this.fullName,
        universityName = this.universityName,
        description = this.description,
        avatarUri = this.avatarUri,
        phoneNumber = this.phoneNumber
    )
}

fun UserEntity.toDomain(): User {
    return User(
        userId = this.userId,
        username = this.username,
        password = this.password,
        email = this.email,
        fullName = this.fullName,
        universityName = this.universityName,
        description = this.description,
        avatarUri = this.avatarUri,
        phoneNumber = this.phoneNumber
    )
}