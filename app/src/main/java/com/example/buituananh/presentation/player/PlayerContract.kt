package com.example.buituananh.presentation.player

import com.example.buituananh.service.MusicState

data class PlayerState(
    val musicState: MusicState? = null
)

sealed interface PlayerIntent {
    data object ClickBack : PlayerIntent
    data object StopPlaying : PlayerIntent
    data object ToggleShuffleMode : PlayerIntent
    data object ToggleRepeatMode : PlayerIntent
    data object ClickPreviousSong : PlayerIntent
    data object ClickNextSong : PlayerIntent
    data object TogglePlayPauseMode : PlayerIntent
    data class DragSlider(val progress: Float) : PlayerIntent
    data object DragSliderEnd : PlayerIntent
}

sealed interface PlayerEffect {
    data object NavigateToHomeScreen : PlayerEffect
}
