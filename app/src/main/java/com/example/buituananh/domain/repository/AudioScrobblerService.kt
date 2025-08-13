package com.example.buituananh.domain.repository

import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Album
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track

interface AudioScrobblerRepository {
    suspend fun getTopAlbums(): Result<List<Album>, Exception>
    suspend fun getTopTracks(): Result<List<Track>, Exception>
    suspend fun getTopArtist(): Result<List<Artist>, Exception>
}
