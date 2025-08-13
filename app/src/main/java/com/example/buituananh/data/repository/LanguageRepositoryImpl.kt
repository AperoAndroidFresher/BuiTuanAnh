package com.example.buituananh.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.buituananh.domain.repository.LanguageRepository
import com.example.buituananh.languageDataStore
import com.example.buituananh.util.LanguageHelper
import com.example.buituananh.util.UserPrefsKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor( 
    @ApplicationContext val context: Context,
) : LanguageRepository {
    override val languageFlow: Flow<String?>
        get() = context.languageDataStore.data
            .map { it[UserPrefsKey.LANGUAGE_STATE] }

    override suspend fun changePrefsLanguage(language: String) {
        context.languageDataStore.edit {
            it[UserPrefsKey.LANGUAGE_STATE] = language
        }
    }

    override fun changeLanguageSystem(language: String) {
        LanguageHelper.changeLanguage(context, language)
    }
}
