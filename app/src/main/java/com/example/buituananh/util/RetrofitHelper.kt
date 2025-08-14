package com.example.buituananh.util

import com.example.buituananh.data.remote.AudioScrobblerService
import com.example.buituananh.data.remote.SongService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val BASE_URL = "https://static.apero.vn"
    private const val AUDIO_URL = "http://ws.audioscrobbler.com/"
    private const val CONNECT_TIMEOUT_MS = 3000L
    private const val READ_TIMEOUT_MS = 3000L
    private const val WRITE_TIMEOUT_MS = 1000L
    
    fun createAudioScrobblerService(): AudioScrobblerService = createAudioScrobblerRetrofit().create(AudioScrobblerService::class.java)
    fun createSongService(): SongService = createSongRetrofit().create(SongService::class.java)
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply { 
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private fun createSongRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createHttpClient())
        .addConverterFactory(GsonConverterFactory.create(buildGson()))
        .build()

    private fun createAudioScrobblerRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(AUDIO_URL)
        .client(createHttpClient())
        .addConverterFactory(GsonConverterFactory.create(buildGson()))
        .build()

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .writeTimeout(WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .build()

    private fun buildGson(): Gson = GsonBuilder().create()
}
