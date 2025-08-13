package com.example.buituananh.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.buituananh.authDataStore
import com.example.buituananh.data.local.AppDatabase
import com.example.buituananh.data.local.mapper.toDomain
import com.example.buituananh.data.local.model.UserEntity
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.User
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.userDataStore
import com.example.buituananh.util.UserPrefsKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @ApplicationContext private val context: Context
) : UserRepository {

    private val userDao = database.userDao()

    override val userIdFlow: Flow<Long?>
        get() = context.userDataStore.data
            .map { it[UserPrefsKey.USER_ID] }

    override suspend fun isRememberedLoginEnabled(): Boolean {
        return context.authDataStore.data
            .map { prefs -> prefs[UserPrefsKey.LOGIN_STATE] ?: false }
            .first()
    }

    override suspend fun setRememberedLoginState(isRemembered: Boolean) {
        context.authDataStore.edit { 
            it[UserPrefsKey.LOGIN_STATE] = isRemembered
        }
    }

    override suspend fun saveUserId(userId: Long) {
        context.userDataStore.edit { prefs ->
            prefs[UserPrefsKey.USER_ID] = userId
        }
    }

    override suspend fun clearUserId() {
        context.userDataStore.edit {
            it.remove(UserPrefsKey.USER_ID)
        }
    }

    override suspend fun insertUser(user: UserEntity): Result<String, Exception> =
        try {
            userDao.insertUser(user)
            Result.Success("Successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override suspend fun assertLogin(
        username: String,
        password: String
    ): Result<User, Exception> {
        return try {
            val user = userDao.assertLogin(username, password)
            if (user == null) Result.Failure(Exception("Username or password is not matching")) else Result.Success(
                user.toDomain()
            )
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun userRegistration(user: UserEntity): Result<String, Exception> {
        return try {
            val isAccountExist =
                userDao.assertLogin(username = user.username, password = user.password) != null
            if (!isAccountExist) {
                userDao.insertUser(user)
                Result.Success("Register successfully")
            } else {
                Result.Failure(Exception("Username is already existed"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


    override suspend fun updateUser(user: UserEntity): Result<String, Exception> =
        try {
            userDao.updateUser(user)
            Result.Success("Successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override fun getUserById(userId: Long): Flow<User> =
        userDao.getUserById(id = userId).mapNotNull {
            it.toDomain()
        }
}
