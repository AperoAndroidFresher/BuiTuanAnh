package com.example.buituananh.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPrefsKey {
    val USER_ID = longPreferencesKey("user_id")
    val LOGIN_STATE = booleanPreferencesKey("login_state")
    val LANGUAGE_STATE = stringPreferencesKey("language")
}
