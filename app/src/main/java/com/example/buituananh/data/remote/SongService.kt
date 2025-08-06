package com.example.buituananh.data.remote

import com.example.buituananh.data.remote.model.SongResponse
import retrofit2.Response
import retrofit2.http.GET

interface SongService {
    
    @GET("/techtrek/Remote_audio.json")
    suspend fun getSongs(): Response<List<SongResponse>>
}
