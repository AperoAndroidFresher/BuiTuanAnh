package com.example.buituananh.data.repository

import com.example.buituananh.data.local.AppDatabase
import com.example.buituananh.data.local.mapper.toEntity
import com.example.buituananh.data.local.mapper.toSong
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SongRepositoryImpl(
    val database: AppDatabase
) : SongRepository {

    private val songDao = database.songDao()

    override suspend fun isSongExisted(filePath: String): Song? {
        return try {
            songDao.isSongExisted(filePath)?.toSong()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertSong(song: Song) : Long {
        return try {
            songDao.insertSong(song.toEntity())
        } catch (e: Exception) {
            -1
        }
    }

    override suspend fun getAllSongs(): Flow<List<Song>> {
        return songDao.getAllSongs().map { list -> list.map { it.toSong() } }
    }
}
