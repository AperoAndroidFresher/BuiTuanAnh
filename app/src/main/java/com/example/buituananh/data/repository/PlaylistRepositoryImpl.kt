package com.example.buituananh.data.repository

import com.example.buituananh.data.database.AppDatabase
import com.example.buituananh.data.mapper.toPlaylist
import com.example.buituananh.data.model.PlaylistEntity
import com.example.buituananh.data.model.PlaylistMusicCrossRef
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

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

    override suspend fun undoDeletePlaylist(playlistId: Long) {
        try {
            playlistDao.undoDeletePlaylist(playlistId)
        } catch (e: Exception) {

        }
    }

    override suspend fun getPlaylistWithSongs(userId: Long): Flow<List<Playlist>> {
        return playlistDao.getPlaylistWithSongsByUserId(
            userId = userId
        ).map { list ->
            list.map { it.toPlaylist() }
        }
    }

    override suspend fun insertSongToPlaylist(
        playlistId: Long,
        songId: Long
    ): Result<String, Exception> {
        return try {
            playlistDao.insertSongToPlaylist(PlaylistMusicCrossRef(playlistId, songId))
            Result.Success("Add successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun deleteSongFromPlaylist(
        playlistId: Long,
        songId: Long
    ): Result<String, Exception> {
        return try {
            playlistDao.deleteSongFromPlaylist(playlistId, songId)
            Result.Success("Delete successfully")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getPlaylistWithSongById(playlistId: Long): Flow<Playlist> {
        return playlistDao.getPlaylistWithSongsById(playlistId).map { it.toPlaylist() }
    }

    override suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean {
        return playlistDao.isSongInPlaylist(playlistId, songId)
    }
}
