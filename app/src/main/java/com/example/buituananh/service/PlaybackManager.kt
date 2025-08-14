package com.example.buituananh.service

import android.content.Context
import android.content.Intent
import com.example.buituananh.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context,
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
        _musicState.update { it.copy(originalQueue = newQueue) }
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
        val state = _musicState.value
        val currentSong = state.currentSong ?: return

        if (state.isShuffleMode) {
            val shuffled = state.originalQueue.shuffled().toMutableList()
            if (shuffled.remove(currentSong)) {
                shuffled.add(0, currentSong)
            }
            _musicState.update { it.copy(playQueue = shuffled) }
        } else {
            _musicState.update { it.copy(playQueue = state.originalQueue) }
        }
    }

    fun toggleRepeatMode() {
        _musicState.update { it.copy(isRepeatMode = !it.isRepeatMode) }
    }

    suspend fun togglePlayPauseMode() {
        _musicState.update { it.copy(isPlaying = !it.isPlaying) }
        val isPlaying = _musicState.value.isPlaying
        if (isPlaying) {
            playSong()
        } else {
            pauseSong()
        }
    }

    fun launchService() {
        if (firstLaunchService) {
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
                originalQueue = emptyList(),
                currentSong = null,
                isPlaying = false,
                isRepeatMode = false,
                isShuffleMode = false,
                isCancel = true,
                playType = null,
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

    suspend fun playNextSong(userAction: Boolean = false) {
        val state = _musicState.value
        val queue = if (state.isShuffleMode) state.playQueue else state.originalQueue
        val currentSong = state.currentSong ?: return
        if (queue.isEmpty()) return

        val currentIndex = queue.indexOf(currentSong)
        val isLastSong = currentIndex == queue.lastIndex

        if (!userAction && isLastSong && !state.isRepeatMode) {
            _musicState.update {
                it.copy(
                    isPlaying = false,
                    progress = 0,
                    currentSong = currentSong
                )
            }
            sendEvent(PlaybackServiceEvent.PauseSong)
            return
        }

        val nextIndex = if (isLastSong) 0 else currentIndex + 1
        queue.getOrNull(nextIndex)?.let { nextSong ->
            _musicState.update { it.copy(currentSong = nextSong, isPlaying = true) }
            sendEvent(PlaybackServiceEvent.StartSong(nextSong))
        }
    }

    suspend fun playPreviousSong() {
        val currentSong = _musicState.value.currentSong
        val queue = _musicState.value.originalQueue
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
