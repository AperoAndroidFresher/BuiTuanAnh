package com.example.buituananh.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.buituananh.R

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


enum class Language(val languageCode: String) {
    English("en"),
    Korean("ko"),
    French("fr"),
    Vietnamese("vi"),
    Alien("alien");
}

fun String.convertToLanguage(context: Context): String {
    return when (this) {
        "en" -> context.getString(R.string.english)
        "ko" -> context.getString(R.string.korean)
        "fr" -> context.getString(R.string.french)
        "vi" -> context.getString(R.string.vietnamese)
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
