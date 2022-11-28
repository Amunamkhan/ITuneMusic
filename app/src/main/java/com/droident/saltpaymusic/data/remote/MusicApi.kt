package com.droident.saltpaymusic.data.remote


import com.droident.saltpaymusic.data.remote.dto.AlbumResponse
import retrofit2.http.GET

interface MusicApi {


    @GET("/us/rss/topalbums/limit=100/json")
    suspend fun getAlbums(
    ): AlbumResponse


    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}