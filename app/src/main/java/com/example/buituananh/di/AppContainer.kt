package com.example.buituananh.di

import android.content.Context
import com.example.buituananh.data.local.AppDatabase
import com.example.buituananh.data.repository.PlaylistRepositoryImpl
import com.example.buituananh.data.repository.SongRepositoryImpl
import com.example.buituananh.data.repository.UserRepositoryImpl
import com.example.buituananh.domain.repository.PlaylistRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.domain.usecase.FetchAndCacheSongsUseCase
import com.example.buituananh.util.RetrofitHelper

class AppContainer(
    val context: Context
) {

    private val database = AppDatabase.getInstance(context)
    private val songService = RetrofitHelper.createSongService()

    val userRepository: UserRepository = UserRepositoryImpl(database, context)
    val playlistRepository: PlaylistRepository = PlaylistRepositoryImpl(database)
    val songRepository: SongRepository = SongRepositoryImpl(context, database, songService)

    val fetchAndCacheSongsUseCase = FetchAndCacheSongsUseCase(songRepository)
}
