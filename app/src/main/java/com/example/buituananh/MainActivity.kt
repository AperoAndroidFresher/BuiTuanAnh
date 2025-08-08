package com.example.buituananh

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.buituananh.presentation.MyApp
import com.example.buituananh.presentation.player.PlayerEffect
import com.example.buituananh.presentation.player.PlayerIntent
import com.example.buituananh.presentation.player.PlayerState
import com.example.buituananh.service.PlayerService
import com.example.buituananh.util.Util
import com.example.buituananh.util.toLongDuration
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class MainActivity : ComponentActivity() {

    private lateinit var progressReceiver: BroadcastReceiver
    
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                1
            )
        }
        
        val appContainer = (application as MusicApplication).appContainer
        val playerViewModel = appContainer.playerViewModel
        
        startForegroundService(Intent(this@MainActivity, PlayerService::class.java))
        progressReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                p1?.let { 
                    val progressValue = it.getLongExtra("progress", 0L)
                    Log.d("progress", "onReceive: $progressValue")
                    playerViewModel.onIntent(PlayerIntent.UpdateProgress(progressValue))
                }
            }
        }
        setContent {
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    playerViewModel.effect.collect { effect ->
                        when (effect) {
                            is PlayerEffect.StartSong -> {
                                sendMusicBroadcast(effect.playerState)
                                Log.d("MainActivity", "onCreate: ${effect.playerState}")
                            }

                            is PlayerEffect.PauseSong -> {
                                sendMusicBroadcast(effect.playerState)
                            }

                            is PlayerEffect.PlaySong -> {
                                sendMusicBroadcast(effect.playerState)
                            }

                            is PlayerEffect.SeekDuration -> {
                                Log.d("MainActivity", "send progress")
                                sendSliderBroadcast(effect.nextProgress)
                            }
                        }
                    }
                }
            }
            val state = playerViewModel.musicState.collectAsStateWithLifecycle().value
            LaunchedEffect(state.playerState?.progress) {
                snapshotFlow { state.playerState?.progress }
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collect { progress ->
                        val duration = state.playerState?.music?.duration?.toLongDuration() ?: 0L
                        Log.d("MainActivity", "onCreate: $progress, $duration")
                        if (progress >= (duration - 500)) {
                            playerViewModel.onIntent(PlayerIntent.Next)
                        }
                    }
            }
            MyApp(
                contentResolver = this@MainActivity.contentResolver,
                appContainer = appContainer,
            )
        }
    }
    
    private fun sendSliderBroadcast(progress: Long) {
        val intent = Intent(Util.SLIDER_CHANNEL).apply { 
            `package` = packageName
            putExtra(Util.SLIDER, progress)
        }
        sendBroadcast(intent)
    }

    private fun sendMusicBroadcast(playerState: PlayerState) {
        val intent = Intent(Util.PLAYER_STATE_CHANNEL).apply {
            setPackage(packageName)
            putExtra(Util.PLAYER, playerState)
        }
        sendBroadcast(intent)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(progressReceiver, IntentFilter(Util.PROGRESS_CHANNEL), RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(progressReceiver)
    }
  
}

