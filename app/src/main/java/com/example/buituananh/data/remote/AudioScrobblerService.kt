package com.example.buituananh.data.remote

import com.example.buituananh.data.remote.model.response.TopAlbumsResponse
import com.example.buituananh.data.remote.model.response.TopTracksResponse
import com.example.buituananh.data.remote.model.response.artists.TopArtistsResponse
import com.example.buituananh.util.Utils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioScrobblerService {
    
    @GET("2.0/")
    suspend fun getTopAlbums(
        @Query("api_key") apiKey: String = Utils.API_KEY,
        @Query("format") format: String = "json",
        @Query("method") method: String = "artist.getTopAlbums",
        @Query("mbid") mbid: String = "f9b593e6-4503-414c-99a0-46595ecd2e23"
    ): Response<TopAlbumsResponse>

    @GET("2.0/")
    suspend fun getTopTracks(
        @Query("api_key") apiKey: String = Utils.API_KEY,
        @Query("format") format: String = "json",
        @Query("method") method: String = "artist.getTopTracks",
        @Query("mbid") mbid: String = "f9b593e6-4503-414c-99a0-46595ecd2e23"
    ): Response<TopTracksResponse>

    @GET("2.0/")
    suspend fun getTopArtists(
        @Query("api_key") apiKey: String = Utils.API_KEY,
        @Query("format") format: String = "json",
        @Query("method") method: String = "chart.gettopartists",
    ): Response<TopArtistsResponse>
}
