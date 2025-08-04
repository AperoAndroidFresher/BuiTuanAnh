package com.example.buituananh.data.repository

import com.example.buituananh.data.database.AppDatabase
import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val database: AppDatabase
) : PlaylistRepository {

    private val playlistDao = database.playlistDao()

    override suspend fun insertPlaylist(playlist: PlaylistEntity): Result<String, Exception> {
        return try {
            playlistDao.insertPlaylistByUserId(playlist)
            Result.Success("Add playlist successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun deletePlaylist(playlistId: Long): Result<String, Exception> {
        return try {
            playlistDao.deletePlaylist(playlistId)
            Result.Success("Delete playlist successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun renamePlaylist(
        playlistId: Long,
        newTitle: String
    ): Result<String, Exception> {
        return try {
            playlistDao.renamePlaylist(playlistId, newTitle)
            Result.Success("Rename playlist successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override fun getPlaylistsByUserId(userId: Long): Flow<List<PlaylistEntity>> {
        return playlistDao.getPlaylistByUserId(userId = userId)
    }

    override fun getPlaylistById(userId: Long, playlistId: Long): Flow<PlaylistEntity?> {
        return playlistDao.getPlaylistById(userId = userId, playlistId = playlistId)
    }
}
