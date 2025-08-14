package com.example.buituananh.domain.usecase

import android.util.Log
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.FileRepository
import com.example.buituananh.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FetchAndCacheSongsUseCase(
    private val songRepository: SongRepository,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(): Result<Flow<List<Song>>, Exception> {
        val remoteSongsFlow = songRepository.getRemoteSongsFromRoom()
        val isEmpty = remoteSongsFlow.first().isEmpty()

        if (isEmpty) {
            Log.d("Usecase", "invoke: Load from networking")
            return when (val networkResult = songRepository.getNetworkSongs()) {
                is Result.Success -> {
                    val updatedSongs = alterUrlPathToFilePath(networkResult.data)
                    songRepository.insertSongs(updatedSongs)

                    Result.Success(songRepository.getRemoteSongsFromRoom())
                }

                is Result.Failure -> {
                    Result.Failure(networkResult.error)
                }
            }
        } else {
            Log.d("Usecase", "invoke: Load from cache")
            return Result.Success(remoteSongsFlow)
        }
    }

    
    private suspend fun alterUrlPathToFilePath(songs: List<Song>): List<Song> {
        val newSongs = songs.mapNotNull { song ->
            val url = song.filePath ?: return@mapNotNull null

            val file = fileRepository.saveAudioFileToExternalStorage(
                urlPath = url,
                fileName = "${song.title}-${song.artist}"
            )
            val imageUri = fileRepository.getEmbeddedImageFromAudio(file)
            song.copy(filePath = file.path, imageUri = imageUri)
        }
        return newSongs
    }
}
