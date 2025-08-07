package com.example.buituananh.domain.repository

import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.Flow
import java.io.File

interface SongRepository {

    suspend fun isSongExisted(filePath: String): Song?

    suspend fun insertSong(song: Song): Long
    
    suspend fun insertSongs(songs: List<Song>)

    suspend fun getLocalSongsFromRoom(): Flow<List<Song>>

    suspend fun getNetworkSongs(): Result<List<Song>, Exception>
    
    suspend fun getRemoteSongsFromRoom(): Flow<List<Song>>
    
    suspend fun saveSongToInternalStorage(urlPath: String, fileName: String): File
}

