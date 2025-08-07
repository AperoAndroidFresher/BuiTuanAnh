package com.example.buituananh.data.repository

import android.content.Context
import android.util.Log
import com.example.buituananh.data.local.AppDatabase
import com.example.buituananh.data.local.mapper.toEntity
import com.example.buituananh.data.local.mapper.toSong
import com.example.buituananh.data.remote.SongService
import com.example.buituananh.data.remote.mapper.toSong
import com.example.buituananh.data.util.Result
import com.example.buituananh.data.util.safeCall
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.util.SongSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class SongRepositoryImpl(
    private val context: Context,
    private val database: AppDatabase,
    private val songService: SongService,
) : SongRepository {

    private val songDao = database.songDao()

    override suspend fun isSongExisted(filePath: String): Song? {
        return try {
            songDao.isSongExisted(filePath)?.toSong()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertSong(song: Song): Long {
        return try {
            songDao.insertSong(song.toEntity())
        } catch (e: Exception) {
            -1
        }
    }

    override suspend fun insertSongs(songs: List<Song>) {
        try {
             songDao.insertSongs(songs.map { it.toEntity().copy(songId = null) })
        } catch (e: Exception) {
            Log.d("SongRepo", "insertSongs: $e")
        }
    }

    override suspend fun getLocalSongsFromRoom(): Flow<List<Song>> {
        return songDao.getAllSongs()
            .map { list -> list.filter { it.songSource == SongSource.LOCAL }.map { it.toSong() } }
    }

    override suspend fun getNetworkSongs(): Result<List<Song>, Exception> {
        val response = safeCall {
            songService.getSongs()
        }
        return when (response) {
            is Result.Failure -> response
            is Result.Success -> Result.Success(response.data.map { it.toSong() })
        }
    }

    override suspend fun getRemoteSongsFromRoom(): Flow<List<Song>> {
        return songDao.getAllSongs()
            .map { list -> list.filter { it.songSource == SongSource.REMOTE }.map { it.toSong() } }
    }

    override suspend fun saveSongToInternalStorage(urlPath: String, fileName: String): File {
        val directory = File(context.filesDir, "internal_storage")

        if (directory.exists() && !directory.isDirectory) {
            directory.delete()
        }
        if (!directory.exists()) {
            directory.mkdir()
        }

        val file = File(directory, "$fileName.mp3")
        withContext(Dispatchers.IO) {
            val url = URL(urlPath)
            url.openStream().use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }
}
