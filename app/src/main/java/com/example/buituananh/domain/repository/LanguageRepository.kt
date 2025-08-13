package com.example.buituananh.domain.repository

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    val languageFlow: Flow<String?>
    suspend fun changePrefsLanguage(language: String)
    fun changeLanguageSystem(language: String)
}
