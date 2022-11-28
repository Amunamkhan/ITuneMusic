package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class Feed(

    @field:Json(name = "entry") val albums: List<AlbumDto>,


    )