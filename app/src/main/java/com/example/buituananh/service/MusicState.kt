package com.example.buituananh.service

import com.example.buituananh.domain.model.Song

data class MusicState(
    val queue: List<Song> = emptyList(),
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val progress: Long = 0L,
    val isRepeatMode: Boolean = false,
    val isShuffleMode: Boolean = false,
    val isCancel: Boolean = true
)
