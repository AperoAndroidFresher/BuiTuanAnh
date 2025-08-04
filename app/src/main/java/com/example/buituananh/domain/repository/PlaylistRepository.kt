package com.example.buituananh.domain.repository

import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.data.model.PlaylistWithSongs
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: PlaylistEntity): Result<String, Exception>

    suspend fun deletePlaylist(playlistId: Long): Result<String, Exception>

    suspend fun renamePlaylist(playlistId: Long, newTitle: String): Result<String, Exception>

    suspend fun undoDeletePlaylist(playlistId: Long)

    suspend fun getPlaylistWithSongs(userId: Long): Flow<List<Playlist>>

    suspend fun insertSongToPlaylist(playlistId: Long, songId: Long): Result<String, Exception>

    suspend fun deleteSongFromPlaylist(playlistId: Long, songId: Long): Result<String, Exception>

    suspend fun getPlaylistWithSongById(playlistId: Long) : Flow<Playlist>
}