package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class Attributes(
    @field:Json(name = "im:id") val imId: Int?,
    @field:Json(name = "label") val label: String?,
    @field:Json(name = "amount") val amount: String?,
    @field:Json(name = "currency") val currency: String?,


    )