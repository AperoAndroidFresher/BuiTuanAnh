package com.example.buituananh.presentation.home.components

import com.example.buituananh.domain.model.User

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

sealed interface HomeIntent {
    data object LoadUserData : HomeIntent
    data object ClickSetting : HomeIntent
    data object ClickProfile : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToSettingScreen : HomeEffect
    data object NavigateToProfileScreen : HomeEffect
    data class ShowToast(val message: String) : HomeEffect
}

