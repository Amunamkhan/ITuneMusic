package com.droident.saltpaymusic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* favorite table  to  persist favorites
* */
@Entity(tableName = "favorite_albums")
data class FavoriteAlbumEntity(
    @PrimaryKey val albumId: Int
)