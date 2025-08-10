package com.example.buituananh.service

import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()
    
    private val _command = MutableSharedFlow<PlaybackEvent>()
    val command = _command.asSharedFlow()
    
    fun updateQueue(newQueue: List<Song>) {
        _playerState.update { it.copy(queue = newQueue) }
    }
    
    fun updateSong(newSong: Song) {
        _playerState.update { it.copy(currentSong = newSong) }
    }

    fun updateIsPlaying(isPlaying: Boolean) {
        _playerState.update { it.copy(isPlaying = isPlaying) }
    } 
    
    fun updateProgress(progress: Long) {
        _playerState.update { it.copy(progress = progress) }
    }
    
    suspend fun startSong() {
        val currentSong = _playerState.value.currentSong
        _playerState.update { it.copy(isPlaying = true) }
        currentSong?.let {
            sendEvent(PlaybackEvent.StartSong(it))
        }
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
        val currentSong = _playerState.value.currentSong
        val queue = _playerState.value.queue
        val currentIndex = queue.indexOf(currentSong)
        
        val nextIndex: Int = if(_playerState.value.isRepeatMode) {
            currentIndex
        } else {
            if(_playerState.value.isShuffleMode) {
                var randomIndex = -1
                while(randomIndex != currentIndex) {
                    randomIndex = queue.indices.random()
                }
                randomIndex
            } else {
                if (currentIndex != -1 && currentIndex < queue.size - 1) {
                    currentIndex + 1
                } else {
                    0
                }
            }
        }

        queue.getOrNull(nextIndex)?.let { nextSong ->
            _playerState.update { it.copy(currentSong = nextSong, isPlaying = true, isRepeatMode = false) }
            sendEvent(PlaybackEvent.StartSong(nextSong))
        }
    }
    
    suspend fun playPreviousSong() {
        val currentSong = _playerState.value.currentSong
        val queue = _playerState.value.queue
        val currentIndex = queue.indexOf(currentSong)
        
        val previousIndex = if(currentIndex != -1) {
            if(currentIndex == 0) {
                queue.size - 1
            } else {
                currentIndex - 1
            }
        } else {
            0
        }
        
        queue.getOrNull(previousIndex)?.let { prevSong ->
            _playerState.update { it.copy(currentSong = prevSong, isPlaying = true) }
            sendEvent(PlaybackEvent.StartSong(prevSong))
        }
    }
    
    private suspend fun sendEvent(event: PlaybackEvent) {
        _command.emit(event)
    }
    
}
