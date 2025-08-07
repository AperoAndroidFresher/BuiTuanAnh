package com.example.buituananh

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.buituananh.presentation.MyApp

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appContainer = (application as MusicApplication).appContainer
            MyApp(
                contentResolver = this@MainActivity.contentResolver,
                appContainer = appContainer
            )
        }
    }
}

