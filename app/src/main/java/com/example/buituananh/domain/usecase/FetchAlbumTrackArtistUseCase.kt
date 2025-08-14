package com.example.buituananh.domain.usecase

import com.example.buituananh.data.util.Result
import com.example.buituananh.data.util.onSuccess
import com.example.buituananh.domain.model.Album
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.domain.model.Track
import com.example.buituananh.domain.repository.AudioScrobblerRepository
import javax.inject.Inject

class FetchAlbumTrackArtistUseCase @Inject constructor(
    private val audioScrobblerRepository: AudioScrobblerRepository
) {

    suspend operator fun invoke(): Result<Triple<List<Album>, List<Track>, List<Artist>>, Exception> {
        val albumsResponse = audioScrobblerRepository.getTopAlbums()
        if (albumsResponse is Result.Failure) return Result.Failure(albumsResponse.error)

        val tracksResponse = audioScrobblerRepository.getTopTracks()
        if (tracksResponse is Result.Failure) return Result.Failure(tracksResponse.error)

        val artistsResponse = audioScrobblerRepository.getTopArtist()
        if (artistsResponse is Result.Failure) return Result.Failure(artistsResponse.error)

        val albums = (albumsResponse as Result.Success).data
        val tracks = (tracksResponse as Result.Success).data
        val artists = (artistsResponse as Result.Success).data

        return Result.Success(Triple(albums, tracks, artists))
    }
    
}
