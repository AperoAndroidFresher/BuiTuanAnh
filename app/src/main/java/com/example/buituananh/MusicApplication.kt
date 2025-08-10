package com.example.buituananh

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.buituananh.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            Utils.MEDIA_CHANNEL,
            Utils.MEDIA_CHANNEL.toString(),
            NotificationManager.IMPORTANCE_HIGH
        )
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
    
}
