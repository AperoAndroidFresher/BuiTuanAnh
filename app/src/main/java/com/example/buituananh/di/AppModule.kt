package com.example.buituananh.di

import android.content.Context
import com.example.buituananh.data.local.AppDatabase
import com.example.buituananh.data.remote.SongService
import com.example.buituananh.domain.repository.FileRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.usecase.FetchAndCacheSongsUseCase
import com.example.buituananh.service.PlaybackManager
import com.example.buituananh.util.RetrofitHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlaybackManager(): PlaybackManager = PlaybackManager()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getInstance(context)
    
    @Provides
    @Singleton
    fun provideSongService(): SongService = RetrofitHelper.createSongService()

    @Provides
    @Singleton
    fun provideFetchAndCacheSongUseCase(
        songRepository: SongRepository,
        fileRepository: FileRepository,
    ): FetchAndCacheSongsUseCase = FetchAndCacheSongsUseCase(songRepository, fileRepository)
}
