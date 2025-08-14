package com.example.buituananh.di

import android.content.Context
import com.example.buituananh.data.local.AppDatabase
import com.example.buituananh.data.remote.AudioScrobblerService
import com.example.buituananh.data.remote.SongService
import com.example.buituananh.domain.repository.AudioScrobblerRepository
import com.example.buituananh.domain.repository.FileRepository
import com.example.buituananh.domain.repository.LanguageRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.domain.usecase.ApplyLanguageUseCase
import com.example.buituananh.domain.usecase.FetchAlbumTrackArtistUseCase
import com.example.buituananh.domain.usecase.FetchAndCacheSongsUseCase
import com.example.buituananh.domain.usecase.LoadLanguageUseCase
import com.example.buituananh.domain.usecase.LoadUserDataUseCase
import com.example.buituananh.service.PlaybackManager
import com.example.buituananh.util.RetrofitHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAudioScrobblerService(): AudioScrobblerService = RetrofitHelper.createAudioScrobblerService()
    
    @Provides
    @Singleton
    fun provideSongService(): SongService = RetrofitHelper.createSongService()

    @Provides
    @Singleton
    fun providePlaybackManager(@ApplicationContext context: Context): PlaybackManager = PlaybackManager(context)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getInstance(context)
    
    @Provides
    @Singleton
    fun provideFetchAndCacheSongUseCase(
        songRepository: SongRepository,
        fileRepository: FileRepository,
    ): FetchAndCacheSongsUseCase = FetchAndCacheSongsUseCase(songRepository, fileRepository)
    
    @Provides
    @Singleton
    fun provideLoadUserDataUserCase(
        userRepository: UserRepository
    ): LoadUserDataUseCase = LoadUserDataUseCase(userRepository)
    
    @Provides
    @Singleton
    fun provideLoadLanguageUseCase(
        languageRepository: LanguageRepository
    ): LoadLanguageUseCase = LoadLanguageUseCase(languageRepository)

    @Provides
    @Singleton
    fun provideApplyLanguageUseCase(
        languageRepository: LanguageRepository
    ): ApplyLanguageUseCase = ApplyLanguageUseCase(languageRepository)
    
    @Provides
    @Singleton
    fun provideFetchAlbumTrackArtistUseCase(
        audioScrobblerRepository: AudioScrobblerRepository
    ): FetchAlbumTrackArtistUseCase = FetchAlbumTrackArtistUseCase(audioScrobblerRepository)
}
