package com.example.buituananh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buituananh.data.local.model.SongEntity
import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("""
        SELECT *
        FROM musics
        WHERE data = :data
    """)
    suspend fun isSongExisted(data: String): SongEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity) : Long

    @Query("""
        SELECT *
        FROM musics
    """)
    fun getAllSongs(): Flow<List<SongEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

}
