package com.example.buituananh.data.repository

import com.example.buituananh.data.database.AppDatabase
import com.example.buituananh.domain.repository.SongRepository

class SongRepositoryImpl(
    val database: AppDatabase
) : SongRepository {
}