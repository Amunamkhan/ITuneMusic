package com.droident.saltpaymusic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
* Album database for persist
* */
@Entity
data class AlbumEntity(
    val name: String,
    val author: String,
    val price: String,
    val currency: String,
    val image: String,
    val albumId: Int,


    @PrimaryKey val id: Int? = null
)