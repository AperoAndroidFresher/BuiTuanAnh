package com.example.buituananh.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buituananh.service.PlaybackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackManager: PlaybackManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()
    
    private val _effect = Channel<PlayerEffect>()
    val effect = _effect.receiveAsFlow()
    
    init {
        viewModelScope.launch {
            playbackManager.playerState.collect { musicState ->
                _state.update { it.copy(musicState = musicState) }
            }
        }
    }
    
    fun onIntent(intent: PlayerIntent) {
        when(intent) {
            PlayerIntent.ClickBack -> clickBack()
            PlayerIntent.ClickNextSong -> clickNextSong()
            PlayerIntent.ClickPreviousSong -> clickPreviousSong()
            is PlayerIntent.DragSlider -> dragSlider(intent.progress)
            PlayerIntent.StopPlaying -> stopPlaying()
            PlayerIntent.TogglePlayPauseMode -> togglePlayPauseMode()
            PlayerIntent.ToggleRepeatMode -> toggleRepeatMode()
            PlayerIntent.ToggleShuffleMode -> toggleShuffleMode()
            PlayerIntent.DragSliderEnd -> dragSliderEnd()
        }
    }

    private fun dragSliderEnd() {
        viewModelScope.launch { 
            playbackManager.dragSliderEnd()
        }
    }

    private fun toggleShuffleMode() {
        playbackManager.toggleShuffleMode()        
    }

    private fun toggleRepeatMode() {
        playbackManager.toggleRepeatMode()        
    }

    private fun togglePlayPauseMode() {
        viewModelScope.launch {
            playbackManager.togglePlayPauseMode()
        }
    }

    private fun stopPlaying() {
        viewModelScope.launch {
            sendEffect(PlayerEffect.NavigateToHomeScreen)
            playbackManager.stopPlaying()
        }
    }

    private fun dragSlider(progress: Float) {
        viewModelScope.launch { 
            playbackManager.dragSlider(progress)
        }
    }

    private fun clickPreviousSong() {
       viewModelScope.launch { 
           playbackManager.playPreviousSong()
       }
    }

    private fun clickNextSong() {
        viewModelScope.launch {
            playbackManager.playNextSong(userAction = true)
        }
    }

    private fun clickBack() {
        sendEffect(PlayerEffect.NavigateToHomeScreen)
    }

    private fun sendEffect(effect: PlayerEffect) {
        viewModelScope.launch { 
            _effect.send(effect)
        }
    }
    
}
