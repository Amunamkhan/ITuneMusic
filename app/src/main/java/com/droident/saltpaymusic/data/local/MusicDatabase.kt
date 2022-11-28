package com.droident.saltpaymusic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [AlbumEntity::class,FavoriteAlbumEntity::class],
    version = 7
)

abstract class MusicDatabase : RoomDatabase() {
    abstract val dao: MusicDao
}