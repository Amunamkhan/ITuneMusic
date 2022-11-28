package com.droident.saltpaymusic.data.remote.dto


import com.squareup.moshi.Json

data class AlbumDto(

    @field:Json(name = "id") val id: Id,
    @field:Json(name = "im:artist") val artist: Artist,
    @field:Json(name = "im:image") val image: List<Image>,
    @field:Json(name = "im:name") val name: Name,
    @field:Json(name = "im:price") val price: Price,
    @field:Json(name = "title") val title: Title?,
)