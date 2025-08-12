package com.example.buituananh.presentation.setting

import com.example.buituananh.util.Language

data class SettingState(
    val currentLanguageCode: String = "en",
    val backupLanguageCode: String? = null
)

sealed interface SettingIntent {
    data object AcceptLanguage : SettingIntent
    data object CancelLanguage : SettingIntent
    data object LoadLanguage : SettingIntent
    data class OnLanguageChange(val language: Language) : SettingIntent
}

sealed interface SettingEffect {
    data object NavigateToHomeScreen : SettingEffect
    data object PopBack : SettingEffect
}

