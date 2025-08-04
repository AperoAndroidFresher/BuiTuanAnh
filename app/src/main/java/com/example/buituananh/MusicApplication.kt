package com.example.buituananh

import android.app.Application
import com.example.buituananh.di.AppContainer

class MusicApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }

}
