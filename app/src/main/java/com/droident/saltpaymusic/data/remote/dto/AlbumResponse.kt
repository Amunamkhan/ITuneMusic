package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class AlbumResponse(
    @Json(name = "feed")
    val feed: Feed
)