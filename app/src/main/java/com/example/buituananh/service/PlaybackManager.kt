package com.example.buituananh.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.buituananh.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _musicState = MutableStateFlow(MusicState())
    val playerState = _musicState.asStateFlow()

    private val _command = MutableSharedFlow<PlaybackServiceEvent>()
    val command = _command.asSharedFlow()
    
    var firstLaunchService = true
    
    suspend fun dragSliderEnd() {
        sendEvent(PlaybackServiceEvent.DragSlider)
    }
    
    fun updatePlayType(playType: PlayType) {
        _musicState.update { it.copy(playType = playType) }
    }
    
    fun updatePlaylistId(id: Long?) {
        _musicState.update { it.copy(playlistId = id) }
    }
    
    fun dragSlider(progress: Float) {
        _musicState.update { it.copy(progress = progress.toLong()) }
    }
    
    fun updateQueue(newQueue: List<Song>) {
        _musicState.update { it.copy(queue = newQueue) }
    }

    fun updateSong(newSong: Song) {
        _musicState.update { it.copy(currentSong = newSong) }
    }

    fun updateIsPlaying(isPlaying: Boolean) {
        _musicState.update { it.copy(isPlaying = isPlaying) }
    }

    fun updateProgress(progress: Long) {
        _musicState.update { it.copy(progress = progress) }
    }
    
    fun toggleShuffleMode() {
        _musicState.update { it.copy(isShuffleMode = !it.isShuffleMode) }
    }

    fun toggleRepeatMode() {
        _musicState.update { it.copy(isRepeatMode = !it.isRepeatMode) }
    }

    suspend fun togglePlayPauseMode() {
        _musicState.update { it.copy(isPlaying = !it.isPlaying) }
        val isPlaying = _musicState.value.isPlaying
        if(isPlaying) {
            playSong()
        } else {
            pauseSong()
        }
    }
    
    fun launchService() {
        if(firstLaunchService) {
            context.startForegroundService(Intent(context, MusicService::class.java))
            firstLaunchService = false
        }
    }

    suspend fun startSong() {
        val currentSong = _musicState.value.currentSong
        _musicState.update { it.copy(isPlaying = true, isCancel = false) }
        currentSong?.let {
            sendEvent(PlaybackServiceEvent.StartSong(it))
        }
    }

    suspend fun stopPlaying() {
        _musicState.update {
            it.copy(
                queue = emptyList(),
                currentSong = null,
                isPlaying = false,
                isRepeatMode = false,
                isShuffleMode = false,
                isCancel = true,
                playType = null
            )
        }
        sendEvent(PlaybackServiceEvent.StopPlaying)
    }

    suspend fun playSong() {
        updateIsPlaying(true)
        sendEvent(PlaybackServiceEvent.PlaySong)
    }

    suspend fun pauseSong() {
        updateIsPlaying(false)
        sendEvent(PlaybackServiceEvent.PauseSong)
    }

    suspend fun playNextSong() {
        val currentSong = _musicState.value.currentSong
        val queue = _musicState.value.queue
        val currentIndex = queue.indexOf(currentSong)

        Log.d("PlaybackManager", "playNextSong: ${_musicState.value.isRepeatMode}")
        val nextIndex: Int = when {
            _musicState.value.isRepeatMode -> {
                _musicState.update { it.copy(isRepeatMode = false) }
                currentIndex
            }
            _musicState.value.isShuffleMode -> {
                var randomIndex = currentIndex
                while(randomIndex == currentIndex && queue.size > 1) {
                    randomIndex = queue.indices.random()
                }
                randomIndex
            }
            else -> {
                if (currentIndex != -1 && currentIndex < queue.size - 1) {
                    currentIndex + 1
                } else {
                    0
                }
            }
        }
        Log.d("PlaybackManager", "playNextSong: ${nextIndex}")
        queue.getOrNull(nextIndex)?.let { nextSong ->
            _musicState.update { it.copy(currentSong = nextSong, isPlaying = true, isRepeatMode = false) }
            sendEvent(PlaybackServiceEvent.StartSong(nextSong))
        }
    }

    suspend fun playPreviousSong() {
        val currentSong = _musicState.value.currentSong
        val queue = _musicState.value.queue
        val currentIndex = queue.indexOf(currentSong)

        val previousIndex = if (currentIndex != -1) {
            if (currentIndex == 0) {
                queue.size - 1
            } else {
                currentIndex - 1
            }
        } else {
            0
        }

        queue.getOrNull(previousIndex)?.let { prevSong ->
            _musicState.update { it.copy(currentSong = prevSong, isPlaying = true) }
            sendEvent(PlaybackServiceEvent.StartSong(prevSong))
        }
    }

    private suspend fun sendEvent(event: PlaybackServiceEvent) {
        _command.emit(event)
    }
}
