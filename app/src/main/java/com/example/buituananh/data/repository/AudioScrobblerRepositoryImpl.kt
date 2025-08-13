package com.example.buituananh.data.repository

import com.example.buituananh.data.remote.AudioScrobblerService
import com.example.buituananh.data.remote.mapper.toAlbum
import com.example.buituananh.data.remote.mapper.toArtist
import com.example.buituananh.data.remote.mapper.toTrack
import com.example.buituananh.data.util.Result
import com.example.buituananh.data.util.safeCall
import com.example.buituananh.domain.model.Album
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track
import com.example.buituananh.domain.repository.AudioScrobblerRepository
import javax.inject.Inject

class AudioScrobblerRepositoryImpl @Inject constructor(
    private val service: AudioScrobblerService,
) : AudioScrobblerRepository {
    override suspend fun getTopAlbums(): Result<List<Album>, Exception> {
        val response = safeCall { service.getTopAlbums() }
        return when (response) {
            is Result.Failure -> response
            is Result.Success -> Result.Success(response.data.topAlbums.albumList.map { it.toAlbum() })
        }
    }

    override suspend fun getTopTracks(): Result<List<Track>, Exception> {
        val response = safeCall { service.getTopTracks() }
        return when (response) {
            is Result.Failure -> response
            is Result.Success -> Result.Success(response.data.topTracks.trackList.map { it.toTrack() })
        }
    }

    override suspend fun getTopArtist(): Result<List<Artist>, Exception> {
        val response = safeCall { service.getTopArtists() }
        return when (response) {
            is Result.Failure -> response
            is Result.Success -> Result.Success(response.data.artists.artist.map { it.toArtist() })
        }
    }
}
