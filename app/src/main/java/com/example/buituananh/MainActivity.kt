package com.example.buituananh

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.buituananh.presentation.MyApp
import com.example.buituananh.service.MusicService
import com.example.buituananh.util.UserPrefsKey
import com.example.buituananh.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")
val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "language_prefs")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var newIntent by mutableStateOf<Intent?>(null)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1,
                )
            } else {
                startForegroundService(Intent(this@MainActivity, MusicService::class.java))
            }
        }
        setContent {
            MyApp(newIntent = newIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(Utils.CANCEL_SERVICE).apply { 
            `package` = packageName
        }
        sendBroadcast(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        newIntent = intent
        Log.d("MainActivity", "onNewIntent: Receive intent")
    }
}

