package com.example.buituananh.service

import android.app.Application
import android.content.Context
import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor(
    private val context: Context
) {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()
    
    fun updateQueue(newQueue: List<Song>) {
        _playerState.update { it.copy(queue = newQueue) }
    }
    
    fun updateSong(newSong: Song) {
        _playerState.update { it.copy(currentSong = newSong) }
    }
    
    fun playSong() {
        
    }
    
    fun pauseSong() {
        
    }
    
    fun nextSong() {
        
    }
    
    fun previousSong() {
        
    }
    
}
