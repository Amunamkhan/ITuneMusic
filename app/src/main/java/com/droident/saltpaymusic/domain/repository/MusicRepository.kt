package com.droident.saltpaymusic.domain.repository

import com.droident.saltpaymusic.domain.model.Album
import com.droident.saltpaymusic.util.Resource
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    /**
     * @param fetchFromRemote is use to maintain to get data from local or remote
     * @param query is used to maintain advance search
     */

    suspend fun getAlbums(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<Album>>>
    //Todo item show on favorite screen
    suspend fun getFavoriteAlbums(): Flow<Resource<List<Album>>>
    suspend fun getFavorites(): Flow<Resource<Set<Int>>>
    suspend fun addOrRemoveAlbumToFavorite(albumId: Int, isFavorite:Boolean)

}