package com.example.buituananh.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

     @Insert(onConflict = OnConflictStrategy.ABORT)
     suspend fun insertPlaylistByUserId(playlist: PlaylistEntity)

     @Query("""
         UPDATE playlists
         SET isDeleted = 1
         WHERE playlistId = :playlistId
     """)
     suspend fun deletePlaylist(playlistId: Long)

     @Query("""
          UPDATE playlists
          SET isDeleted = 0
          WHERE playlistId = :playlistId
     """)
     suspend fun undoDeletePlaylist(playlistId: Long)

     @Query("""
         UPDATE playlists
         SET title = :newTitle
         WHERE playlistId = :playlistId
     """)
     suspend fun renamePlaylist(playlistId: Long, newTitle: String)

     @Query("""
         SELECT * 
         FROM playlists
         WHERE ownerId = :userId AND isDeleted = 0
     """)
     fun getPlaylistByUserId(userId: Long): Flow<List<PlaylistEntity>>

     @Query("""
         SELECT *
         FROM playlists
         WHERE ownerId = :userId AND playlistId = :playlistId AND isDeleted = 0
         LIMIT 1
     """)
     fun getPlaylistById(userId: Long, playlistId: Long): Flow<PlaylistEntity?>

}