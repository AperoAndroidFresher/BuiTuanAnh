package com.example.buituananh

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.buituananh.di.AppContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApplication : Application() {

    lateinit var appContainer: AppContainer
    private val CHANNEL_ID = "MusicPlayerChannel"

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Audio Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
