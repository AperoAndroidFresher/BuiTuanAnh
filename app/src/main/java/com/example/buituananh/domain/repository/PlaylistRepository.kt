package com.example.buituananh.domain.repository

import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.data.util.Result
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: PlaylistEntity) : Result<String, Exception>

    suspend fun deletePlaylist(playlistId: Long) : Result<String, Exception>

    suspend fun renamePlaylist(playlistId: Long, newTitle: String) : Result<String, Exception>

    fun getPlaylistsByUserId(userId: Long): Flow<List<PlaylistEntity>>

    fun getPlaylistById(userId: Long, playlistId: Long): Flow<PlaylistEntity?>
}