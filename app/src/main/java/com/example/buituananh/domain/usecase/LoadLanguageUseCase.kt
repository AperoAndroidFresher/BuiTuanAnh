package com.example.buituananh.domain.usecase

import com.example.buituananh.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) {

    suspend operator fun invoke(): Flow<String?> {
        return languageRepository.languageFlow
    }

}
