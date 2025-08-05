package com.example.buituananh.domain.repository

import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {

    suspend fun isSongExisted(filePath: String) : Song?

    suspend fun insertSong(song: Song) : Long

    suspend fun getAllSongs() : Flow<List<Song>>

}