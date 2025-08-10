package com.example.buituananh.service

import com.example.buituananh.domain.model.Song

sealed interface PlaybackEvent {
    data object PlaySong : PlaybackEvent
    data object PauseSong : PlaybackEvent
    data object NextSong : PlaybackEvent
    data object PreviousSong : PlaybackEvent
    data class StartSong(val song: Song) : PlaybackEvent
}
