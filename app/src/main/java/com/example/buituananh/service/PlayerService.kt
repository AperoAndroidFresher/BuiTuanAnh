package com.example.buituananh.service

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.buituananh.MainActivity
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.presentation.player.PlayerAction
import com.example.buituananh.presentation.player.PlayerState
import com.example.buituananh.util.Util
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class PlayerService : Service() {

    private val CHANNEL_ID = "MusicPlayerChannel"

    private lateinit var mediaPlayer: MediaPlayer
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var progressJob: Job? = null
    
    private val playerStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("PlayerService", "onReceive: Receive broadcast from service")
            
            intent?.let {
                val playerState = it.getParcelableExtra<PlayerState>(Util.PLAYER)
                playerState?.let { play ->
                    onPlayerAction(play)
                }
            }
        }
    }
    
    private val sliderReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.d("PlayerService", "onReceive: recevei braodcast")
            p1?.let { 
                val nextProgress = p1.getLongExtra(Util.SLIDER, 0L)
                mediaPlayer.seekTo(nextProgress.toInt())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

        registerReceiver(
            playerStateReceiver,
            IntentFilter(Util.PLAYER_STATE_CHANNEL),
            Context.RECEIVER_NOT_EXPORTED
        )
        
        registerReceiver(
            sliderReceiver,
            IntentFilter(Util.SLIDER_CHANNEL),
            Context.RECEIVER_NOT_EXPORTED
        )
    }

    private fun onPlayerAction(state: PlayerState) {
        coroutineScope.launch {
            when (state.action) {
                PlayerAction.START -> {
                    Log.d("PlayerService", "onPlayerAction: start")
                    mediaPlayer.start()
                    changeMediaSource(state.music)
                }

                PlayerAction.PLAY -> {
                    Log.d("PlayerService", "onPlayerAction: Play")
                    mediaPlayer.start()
                    updateDuration(state.progress?.toFloat() ?: 0f)
                    changeMediaSource(state.music)
                }

                PlayerAction.PAUSE -> {
                    Log.d("PlayerService", "onPlayerAction: Pause")
                    mediaPlayer.pause()
                    progressJob?.cancel()
                }

                PlayerAction.NEXT -> {}
                PlayerAction.PREVIOUS -> {}
                PlayerAction.SHUFFLE -> {}
                PlayerAction.LOOP -> {}
            }
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        stopSelf()
        coroutineScope.cancel()
        progressJob?.cancel()
        unregisterReceiver(playerStateReceiver)
        unregisterReceiver(sliderReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    private fun updateDuration(value: Float) {
        if(value != 0f) {
            mediaPlayer.seekTo(value.roundToInt())
        }
    }
    
    private fun updateProgress() {
        progressJob?.cancel()
        progressJob = coroutineScope.launch {
            while(isActive && mediaPlayer.isPlaying) {
                broadcastProgressState()
                delay(100L)
            }
        }
    }
    
    private fun broadcastProgressState() {
        val progress = mediaPlayer.currentPosition.toLong()
        val intent = Intent(Util.PROGRESS_CHANNEL).apply {
            `package` = packageName
            putExtra("progress", progress)
        }
//        Log.d("PlayerService", "service: $progress")
        sendBroadcast(intent)
    }

    private fun changeMediaSource(song: Song?) {
        song?.let {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(song.filePath)
            mediaPlayer.isLooping = false
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                updateProgress()
            }
            mediaPlayer.prepareAsync()

        }
    }

    private fun startNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingInt: PendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Playing Audio")
            .setContentText(" You audio is playing in the background")
            .setSmallIcon(R.drawable.play)
            .setContentIntent(pendingInt)
            .build()
        startForeground(1, notification)
    }
}
