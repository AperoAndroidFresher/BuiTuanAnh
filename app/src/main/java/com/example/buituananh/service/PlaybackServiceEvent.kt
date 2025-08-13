package com.example.buituananh.service

import com.example.buituananh.domain.model.Song

sealed interface PlaybackServiceEvent {
    data object PlaySong : PlaybackServiceEvent
    data object PauseSong : PlaybackServiceEvent
    data object StopPlaying : PlaybackServiceEvent
    data class StartSong(val song: Song) : PlaybackServiceEvent
    data object DragSlider : PlaybackServiceEvent
}
