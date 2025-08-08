package com.example.buituananh.presentation.player

import android.os.Parcelable
import com.example.buituananh.domain.model.Song
import kotlinx.parcelize.Parcelize

data class MusicState(
    val musicList: List<Song> = emptyList(),
    val playerState: PlayerState? = null,
    val sliderState: Float = 0f,
)

@Parcelize
data class PlayerState(
    val id: Long? = null,
    val music: Song? = null,
    val progress: Long? = null,
    var action: PlayerAction = PlayerAction.START,
) : Parcelable

enum class PlayerAction {
    START,
    PLAY,
    PAUSE,
    NEXT,
    PREVIOUS,
    SHUFFLE,
    LOOP
}

sealed interface PlayerIntent {
    data class SelectSong(val songList: List<Song>, val song: Song) : PlayerIntent
    data object ClickPlay : PlayerIntent
    data object ClickPause : PlayerIntent
    data object Next : PlayerIntent
    data object Previous : PlayerIntent
    data class SliderChange(val duration: Float, val song: Song) : PlayerIntent
    data object Shuffle : PlayerIntent
    data object Loop : PlayerIntent
}

sealed interface PlayerEffect {
    data class StartSong(val playerState: PlayerState) : PlayerEffect
    data class PlaySong(val playerState: PlayerState) : PlayerEffect
    data class PauseSong(val playerState: PlayerState) : PlayerEffect
}
