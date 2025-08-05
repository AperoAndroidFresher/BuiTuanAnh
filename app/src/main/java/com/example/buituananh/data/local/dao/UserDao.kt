package com.example.buituananh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.buituananh.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("""
        SELECT *
        FROM users
        WHERE userId = :id
        LIMIT 1
    """)
    fun getUserById(id: Long) : Flow<UserEntity>

    @Query("""
        SELECT *
        FROM users
        WHERE username = :username AND password = :password
        LIMIT 1
    """)
    suspend fun assertLogin(username: String, password: String) : UserEntity?

}
