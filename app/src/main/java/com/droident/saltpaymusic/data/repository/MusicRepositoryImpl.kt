package com.droident.saltpaymusic.data.repository

import com.droident.saltpaymusic.data.local.FavoriteAlbumEntity
import com.droident.saltpaymusic.data.local.MusicDatabase
import com.droident.saltpaymusic.data.mapper.toAlbum
import com.droident.saltpaymusic.data.mapper.toAlbumEntity
import com.droident.saltpaymusic.data.remote.MusicApi
import com.droident.saltpaymusic.domain.model.Album
import com.droident.saltpaymusic.domain.repository.MusicRepository
import com.droident.saltpaymusic.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val api: MusicApi,
    private val db: MusicDatabase,
) : MusicRepository {
    private val dao = db.dao

    /**
     * @param fetchFromRemote is use to maintain to get data from local or remote
     * @param query is used to maintain advance search
     */
    override suspend fun getAlbums(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<Album>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchAlbums(query)
            emit(Resource.Success(data = localListings.map { it.toAlbum() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getAlbums()
                response.feed.albums
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearAlbums()
                dao.insertAlbums(listings.map { it.toAlbumEntity() })
                // Singe source of truth is presist data
                emit(Resource.Success(data = dao.searchAlbums("").map { it.toAlbum() }))
                emit(Resource.Loading(false))
            }
        }


    }
    /**
     * @return  ids of favorite albums
     */
    //Todo show favorite is facorite screen  list
    override suspend fun getFavoriteAlbums(): Flow<Resource<List<Album>>> {
        return flow {

            emit(Resource.Success(dao.getFavoriteAlbums(dao.getFavorites().map { it.albumId })
                .map { it.toAlbum() }))
        }
    }

    /**
     * @return  ids of favorite item
     */
    override suspend fun getFavorites(): Flow<Resource<Set<Int>>> {
        return flow {
            emit(Resource.Success(dao.getFavorites().map { it.albumId }.toSet()))
        }
    }

    /**
     * @param albumId  is used to save the data in favorite table
     * @param isFavorite using to get difference between already favorite
     */
    override suspend fun addOrRemoveAlbumToFavorite(albumId: Int, isFavorite: Boolean) {

        if(isFavorite){
            dao.removeFavorite(FavoriteAlbumEntity(albumId))
        }else{
            dao.insertFavorite(FavoriteAlbumEntity(albumId))
        }


    }


}

