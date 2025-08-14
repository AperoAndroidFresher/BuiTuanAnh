package com.example.buituananh.service

import com.example.buituananh.domain.model.Song

data class MusicState(
    val originalQueue: List<Song> = emptyList(),
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val progress: Long = 0L,
    val isRepeatMode: Boolean = false,
    val isShuffleMode: Boolean = false,
    val isCancel: Boolean = true,
    val playlistId: Long? = null,
    val playType: PlayType? = null,
    val playQueue: List<Song> = emptyList()
) 

enum class PlayType {
    PREVIEW,
    FOREGROUND
}
