package com.example.buituananh.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.buituananh.MainActivity
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.util.ImageUtils
import com.example.buituananh.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    companion object {
        const val ACTION_PREV = "ACTION_PREV"
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_CANCEL = "ACTION_CANCEL"
    }

    @Inject
    lateinit var playbackManager: PlaybackManager

    private var mediaPlayer: MediaPlayer? = null
    private var currentState: MusicState = MusicState()
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            playbackManager.playerState.collect { newState ->
                Log.d("Service1", "onCreate: ${newState.isPlaying}")
                currentState = newState
            }
        }
        scope.launch {
            playbackManager.command.collect { command ->
                when (command) {
                    PlaybackEvent.NextSong -> nextSong()
                    PlaybackEvent.PauseSong -> pauseSong()
                    PlaybackEvent.PlaySong -> playSong()
                    PlaybackEvent.PreviousSong -> previousSong()
                    is PlaybackEvent.StartSong -> startSong(command.song)
                    PlaybackEvent.StopPlaying -> stopPlaying()
                    PlaybackEvent.DragSlider -> dragSlider()
                }
            }
        }
    }

    private fun dragSlider() {
        val progress = currentState.progress.toInt()
        mediaPlayer?.seekTo(progress)
    }

    private fun stopPlaying() {
        scope.launch {
            mediaPlayer?.reset()
            val notification = createEmptyNotification()
            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mediaPlayer == null) {
            val notification = createEmptyNotification()
            startForeground(1, notification)
        }
        scope.launch {
            when (intent?.action) {
                ACTION_PREV -> playbackManager.playPreviousSong()
                ACTION_PLAY -> playbackManager.playSong()
                ACTION_PAUSE -> playbackManager.pauseSong()
                ACTION_NEXT -> playbackManager.playNextSong()
                ACTION_CANCEL -> stopPlaying()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        scope.cancel()
        job?.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun previousSong() {
    }

    private fun playSong() {
        Log.d("Service1", "PlaySong: ")
        playbackManager.updateIsPlaying(true)
        mediaPlayer?.seekTo(currentState.progress.toInt())
        mediaPlayer?.start()
        notifySong()
    }

    private fun pauseSong() {
        Log.d("Service1", "pauseSong: ")
        mediaPlayer?.pause()
        playbackManager.updateIsPlaying(false)
        notifySong()
    }

    private fun nextSong() {
    }

    private fun updateProgress() {
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        val progress = it.currentPosition.toLong()
                        playbackManager.updateProgress(progress)
                    }
                }
                delay(100L)
            }
        }
    }

    private fun startSong(song: Song) {
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.filePath)
            setOnPreparedListener {
                start()
                updateProgress()
                notifySong()
            }
            setOnCompletionListener {
                scope.launch {
                    playbackManager.playNextSong()
                }
            }
            prepareAsync()
        }
    }

    private fun notifySong() {
        val notification = createNotification()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createNotification(): Notification {
        val queue = currentState.queue
        val song = currentState.currentSong
        val isPlaying = currentState.isPlaying
        Log.d("Service1", "createNotification: $isPlaying")

        val remoteViews = RemoteViews(packageName, R.layout.notification_player)

        remoteViews.setTextViewText(R.id.txt_idx, (queue.indexOf(song) + 1).toString())
        remoteViews.setTextViewText(R.id.txt_total, queue.size.toString())
        remoteViews.setTextViewText(R.id.txt_title, song?.title)
        remoteViews.setTextViewText(R.id.txt_artist, song?.artist)

        val imageBitmap = ImageUtils.getImageFromUri(this, song?.imageUri)
        if (imageBitmap == null) {
            remoteViews.setImageViewResource(R.id.imgCover, R.drawable.default_song)
        } else {
            remoteViews.setImageViewBitmap(R.id.imgCover, imageBitmap)
        }

        remoteViews.setOnClickPendingIntent(R.id.btnPrev, getPendingIntent(ACTION_PREV))
        remoteViews.setOnClickPendingIntent(
            R.id.btnPlayPause,
            getPendingIntent(if (isPlaying) ACTION_PAUSE else ACTION_PLAY),
        )
        remoteViews.setImageViewResource(R.id.btnPlayPause, if (isPlaying) R.drawable.pause else R.drawable.play)
        remoteViews.setOnClickPendingIntent(R.id.btnNext, getPendingIntent(ACTION_NEXT))
        remoteViews.setOnClickPendingIntent(R.id.btnCancel, getPendingIntent(ACTION_CANCEL))

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE,
        )

        return NotificationCompat.Builder(this, Utils.MEDIA_CHANNEL)
            .setSmallIcon(R.drawable.aperologo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setOngoing(isPlaying)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createEmptyNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingInt: PendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, Utils.MEDIA_CHANNEL)
            .setContentTitle("Playing Audio")
            .setContentText(" You audio is playing in the background")
            .setSmallIcon(R.drawable.play)
            .setContentIntent(pendingInt)
            .build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        return PendingIntent.getService(
            this,
            action.hashCode(),
            Intent(this, MusicService::class.java).setAction(action),
            PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
