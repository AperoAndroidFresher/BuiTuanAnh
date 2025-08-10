package com.example.buituananh

import android.app.Application
import com.example.buituananh.di.AppContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }

}
