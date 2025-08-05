package com.example.buituananh.domain.repository

import com.example.buituananh.data.local.model.UserEntity
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userIdFlow: Flow<Long?>

    suspend fun insertUser(user: UserEntity) : Result<String, Exception>

    suspend fun updateUser(user: UserEntity) : Result<String, Exception>

    fun getUserById(userId: Long) : Flow<User>

    suspend fun assertLogin(username: String, password: String) : Result<User, Exception>

    suspend fun userRegistration(user: UserEntity) : Result<String, Exception>

    suspend fun saveUserId(userId: Long)

    suspend fun clearUserId()

}
