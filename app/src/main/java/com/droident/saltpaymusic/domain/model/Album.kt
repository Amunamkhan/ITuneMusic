package com.droident.saltpaymusic.domain.model

data class Album(
    val name: String,
    val author: String,
    val price: String,
    val currency: String,
    val image: String,
    // We are using this id for making album favorite  and unfavorite
    val albumId: Int
    )
