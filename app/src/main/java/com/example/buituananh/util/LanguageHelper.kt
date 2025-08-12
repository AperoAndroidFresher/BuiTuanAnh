package com.example.buituananh.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageHelper {
    fun changeLanguage(context: Context, languageCode: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode)) 
        }
    }
    
    fun getLanguageCode(context: Context): String {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales[0]?.toLanguageTag()?.split("-")?.first() ?: "en"
        } else {
            AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.split("-")?.first() ?: "en"
        }
    }
}


sealed class Language(val languageCode: String) {
    data object English : Language("en")
    data object Korean : Language("ko")
    data object French : Language("fr")
    data object Vietnamese : Language("vi")
    data object Alien : Language("alien")
} 

fun String.convertToLanguage(): String {
    return when(this) {
        "en" -> "English"
        "ko" -> "Korean"
        "fr" -> "French"
        "vi" -> "Vietnamese"
        else -> "Alien"
    }
}

fun String.toLanguageClass(): Language {
    return when(this) {
        "English" -> Language.English
        "Korean" -> Language.Korean
        "French" -> Language.French
        "Vietnamese" -> Language.Vietnamese
        else -> Language.Alien
    }
}
