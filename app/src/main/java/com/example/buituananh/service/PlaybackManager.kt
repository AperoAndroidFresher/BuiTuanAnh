package com.example.buituananh.service

import android.util.Log
import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor() {

    private val _musicState = MutableStateFlow(MusicState())
    val playerState = _musicState.asStateFlow()

    private val _command = MutableSharedFlow<PlaybackEvent>()
    val command = _command.asSharedFlow()
    
    suspend fun dragSliderEnd() {
        sendEvent(PlaybackEvent.DragSlider)
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

    suspend fun startSong() {
        val currentSong = _musicState.value.currentSong
        _musicState.update { it.copy(isPlaying = true, isCancel = false) }
        currentSong?.let {
            sendEvent(PlaybackEvent.StartSong(it))
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
                isCancel = true 
            )
        }
        sendEvent(PlaybackEvent.StopPlaying)
    }

    suspend fun playSong() {
        updateIsPlaying(true)
        sendEvent(PlaybackEvent.PlaySong)
    }

    suspend fun pauseSong() {
        updateIsPlaying(false)
        sendEvent(PlaybackEvent.PauseSong)
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
            sendEvent(PlaybackEvent.StartSong(nextSong))
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
            sendEvent(PlaybackEvent.StartSong(prevSong))
        }
    }

    private suspend fun sendEvent(event: PlaybackEvent) {
        _command.emit(event)
    }
}
