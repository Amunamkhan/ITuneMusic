package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class Image(
    @Json(name = "attributes")
    val attributes: Attributes,
    @Json(name = "label")
    val label: String
)