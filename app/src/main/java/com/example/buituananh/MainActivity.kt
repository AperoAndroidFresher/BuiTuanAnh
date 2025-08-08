package com.example.buituananh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.buituananh.presentation.MyApp
import com.example.buituananh.presentation.player.PlayerEffect
import com.example.buituananh.service.PlayerService
import com.example.buituananh.util.Util
import kotlinx.coroutines.launch

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startForegroundService(Intent(this@MainActivity, PlayerService::class.java))

        setContent {
            val appContainer = (application as MusicApplication).appContainer
            val playerViewModel = appContainer.playerViewModel

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    playerViewModel.effect.collect { effect ->
                        when (effect) {
                            is PlayerEffect.StartSong -> {
                                val intent = Intent(Util.PLAYER_STATE_CHANNEL).apply {
                                    setPackage(packageName)
                                    putExtra(Util.PLAYER, effect.playerState)
                                }
                                sendBroadcast(intent)
                                Log.d("MainActivity", "onCreate: ${effect.playerState}")
                            }

                            is PlayerEffect.PauseSong -> {
                                val intent = Intent(Util.PLAYER_STATE_CHANNEL).apply { 
                                    setPackage(packageName)
                                    putExtra(Util.PLAYER, effect.playerState)
                                }
                                sendBroadcast(intent)
                            }
                            is PlayerEffect.PlaySong -> {
                                val intent = Intent(Util.PLAYER_STATE_CHANNEL).apply {
                                    setPackage(packageName)
                                    putExtra(Util.PLAYER, effect.playerState)
                                }
                                sendBroadcast(intent)
                            }
                        }
                    }
                }
            }
            MyApp(
                contentResolver = this@MainActivity.contentResolver,
                appContainer = appContainer,
            )
        }
    }

    override fun onStart() {
        super.onStart()
    }
}

