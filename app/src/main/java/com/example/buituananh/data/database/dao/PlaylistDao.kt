package com.example.buituananh.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.data.model.PlaylistMusicCrossRef
import com.example.buituananh.data.model.PlaylistWithSongs
import com.example.buituananh.data.model.UserWithPlaylists
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertSongToPlaylist(crossRef: PlaylistMusicCrossRef)

     @Insert(onConflict = OnConflictStrategy.ABORT)
     suspend fun insertPlaylistByUserId(playlist: PlaylistEntity)

     @Query("""
          SELECT EXISTS(
            SELECT 1 FROM playlist_music_cross_ref
            WHERE playlistId = :playlistId AND songId = :songId
          )
     """)
     suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean

     @Query("""
         DELETE FROM playlist_music_cross_ref
          WHERE playlistId = :playlistId AND songId = :songId
     """)
     suspend fun deleteSongFromPlaylist(playlistId: Long, songId: Long)

     @Transaction
     @Query("""
          SELECT *
          FROM playlists
          WHERE playlistId = :playlistId
     """)
     fun getPlaylistWithSongsById(playlistId: Long): Flow<PlaylistWithSongs>

     @Transaction
     @Query("""
          SELECT *
          FROM playlists
          WHERE ownerId = :userId AND isDeleted = 0
     """)
     fun getPlaylistWithSongsByUserId(userId: Long): Flow<List<PlaylistWithSongs>>

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