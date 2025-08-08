package com.example.buituananh.presentation.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buituananh.domain.model.Song
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    private val _musicState = MutableStateFlow(MusicState())
    val musicState = _musicState.asStateFlow()

    private val _effect = Channel<PlayerEffect>()
    val effect = _effect.receiveAsFlow()
    
    fun onIntent(intent: PlayerIntent) {
        when (intent) {
            is PlayerIntent.Loop -> onLoop()
            is PlayerIntent.Next -> onNext()
            is PlayerIntent.ClickPause -> onPause()
            is PlayerIntent.ClickPlay -> onPlay()
            is PlayerIntent.Previous -> onPrevious()
            is PlayerIntent.Shuffle -> onShuffle()
            is PlayerIntent.SliderChange -> onSliderChange(intent.song, intent.duration)
            is PlayerIntent.SelectSong -> start(intent.songList, intent.song)
        }
    }

    private fun onPlay() {
        val playerState = _musicState.value.playerState?.copy(action = PlayerAction.PLAY)
        _musicState.update { it.copy(playerState = playerState) }
        playerState?.let {
            sendEffect(PlayerEffect.PauseSong(playerState))
        }
    }

    private fun onPause() {
        val playerState = _musicState.value.playerState?.copy(action = PlayerAction.PAUSE)
        _musicState.update { it.copy(playerState = playerState) }
        playerState?.let {
            sendEffect(PlayerEffect.PauseSong(playerState))
        }
    }

    private fun onNext() {
    }

    private fun onPrevious() {
    }

    private fun onSliderChange(song: Song, sliderState: Float) {
        val playerState = _musicState.value.playerState?.copy(progress = sliderState.toLong())
        _musicState.update { 
            it.copy(playerState = playerState)
        }
    }

    private fun onLoop() {
    }

    private fun onShuffle() {
    }

    private fun start(songList: List<Song>, song: Song) {
        Log.d("playerviewmodel", "start: ${song.toString()}, ${songList.toString()}")
        _musicState.update {
            it.copy(
                musicList = songList, 
                playerState = (it.playerState ?: PlayerState()).copy(
                    music = song, 
                    action = PlayerAction.START,
                    progress = 0L
                )
            )
        }
        Log.d("playerviewmodel", "${_musicState.value.playerState}")
        sendEffect(PlayerEffect.StartSong(_musicState.value.playerState ?: PlayerState()))
    }
    
    private fun sendEffect(effect: PlayerEffect) {
        viewModelScope.launch { 
            _effect.send(effect)
        }
    }
}
