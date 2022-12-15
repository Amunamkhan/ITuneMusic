package com.droident.saltpaymusic.data.mapper

import com.droident.saltpaymusic.data.local.AlbumEntity
import com.droident.saltpaymusic.data.remote.dto.*
import com.droident.saltpaymusic.domain.model.Album


fun AlbumEntity.toAlbum(): Album {
    return Album(
        name = name,
        author = author,
        price = price,
        currency = currency,
        image = image,
        albumId = albumId
    )
}

fun AlbumDto.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        name = name.label,
        author = artist.label,
        price = price.label,
        currency = price.attributes.currency ?: "",
        image = image[image.size - 1].label,
        albumId = id.attributes.imId?:0
    )



}