package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class Title(
    @Json(name = "label")
    val label: String
)