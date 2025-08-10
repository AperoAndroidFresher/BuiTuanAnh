package com.example.buituananh.di

import com.example.buituananh.data.repository.FileRepositoryImpl
import com.example.buituananh.data.repository.PlaylistRepositoryImpl
import com.example.buituananh.data.repository.SongRepositoryImpl
import com.example.buituananh.data.repository.UserRepositoryImpl
import com.example.buituananh.domain.repository.FileRepository
import com.example.buituananh.domain.repository.PlaylistRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository
    
    @Binds
    @Singleton
    abstract fun provideFileRepository(impl: FileRepositoryImpl): FileRepository
    
    @Binds
    @Singleton
    abstract fun providePlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository
    
    @Binds
    @Singleton
    abstract fun provideSongRepository(impl: SongRepositoryImpl): SongRepository
    
}
