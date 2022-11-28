package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class Artist(
    @Json(name = "label")
    val label: String
)