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
import kotlin.math.roundToLong

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
            is PlayerIntent.SliderChange -> onSliderChange(intent.duration)
            is PlayerIntent.SelectSong -> start(intent.songList, intent.song)
            is PlayerIntent.UpdateProgress -> updateProgress(intent.progress)
            is PlayerIntent.DragSliderEnd -> dragSliderEnd()
        }
    }

    private fun dragSliderEnd() {
        val nextProgress = _musicState.value.playerState?.progress ?: 0L
        Log.d("PlayerViewModel", "dragSliderEnd: $nextProgress")
        sendEffect(PlayerEffect.SeekDuration(nextProgress))
    }

    private fun updateProgress(progress: Long) {
        _musicState.update {
            it.copy(playerState = (it.playerState ?: PlayerState()).copy(progress = progress))
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
        val song = _musicState.value.playerState?.music
        val list = _musicState.value.musicList

        if (list.isEmpty()) return

        val currentIdx = list.indexOf(song)
        val isShuffle = _musicState.value.isShuffle
        val isLoop = _musicState.value.isLoop
        val nextIdx = when {
            isLoop -> {
                _musicState.update { it.copy(isLoop = false) }
                currentIdx.takeIf { it != -1 } ?: 0
            }
            !isShuffle -> {
                if (currentIdx == -1 || currentIdx == list.lastIndex) 0 else currentIdx + 1
            }
            list.size == 1 -> 0
            else -> {
                var randomIdx: Int
                do {
                    randomIdx = list.indices.random()
                } while (randomIdx == currentIdx)
                randomIdx
            }
        }
        val nextSong = list[nextIdx]
        Log.d("PlayerViewModel", "onNext: $nextSong")
        startSong(nextSong)
    }

    private fun onPrevious() {
        val song = _musicState.value.playerState?.music
        val list = _musicState.value.musicList
        
        if(list.isEmpty()) return
        
        val currentIdx = list.indexOf(song)
        val previousIdx = if(currentIdx <= 0) {
            list.size - 1
        } else {
            currentIdx - 1
        }
        val previousSong = list[previousIdx]
        startSong(previousSong)
    }

    private fun onSliderChange(sliderState: Float) {
        updateProgress(sliderState.roundToLong())
    }

    private fun onLoop() {
        _musicState.update { it.copy(isLoop = !it.isLoop) }
    }

    private fun onShuffle() {
        _musicState.update { 
            it.copy(isShuffle = !it.isShuffle)
        }
    }

    private fun startSong(song: Song) {
        _musicState.update {
            it.copy(
                playerState = (it.playerState ?: PlayerState()).copy(
                    music = song,
                    action = PlayerAction.START,
                    progress = 0L,
                ),
            )
        }
        sendEffect(PlayerEffect.StartSong(_musicState.value.playerState ?: PlayerState()))
    }

    private fun start(songList: List<Song>, song: Song) {
        Log.d("playerviewmodel", "start: ${song.toString()}, ${songList.toString()}")
        _musicState.update {
            it.copy(
                musicList = songList,
                playerState = (it.playerState ?: PlayerState()).copy(
                    music = song,
                    action = PlayerAction.START,
                    progress = 0L,
                ),
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
