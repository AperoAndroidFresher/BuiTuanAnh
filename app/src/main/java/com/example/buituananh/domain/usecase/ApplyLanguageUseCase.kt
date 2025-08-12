package com.example.buituananh.domain.usecase

import com.example.buituananh.domain.repository.LanguageRepository
import javax.inject.Inject

class ApplyLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) {
    
    suspend operator fun invoke(language: String) {
        languageRepository.changePrefsLanguage(language)
        languageRepository.changeLanguageSystem(language)
    }
    
}
