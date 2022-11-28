package com.droident.saltpaymusic.data.local

class MusicDaoFake: MusicDao {

    private var albumListings = emptyList<AlbumEntity>()

    override suspend fun insertAlbums(albumListingEntities: List<AlbumEntity>) {
        albumListings = albumListings + albumListingEntities
    }

    override suspend fun clearAlbums() {
        albumListings = emptyList()
    }

    override suspend fun searchAlbums(query: String): List<AlbumEntity> {
        return albumListings.filter {
            it.name.lowercase().contains(query.lowercase())||it.author.contains(query.toLowerCase())
        }
    }

    override suspend fun getFavoriteAlbums(albumIds: List<Int>): List<AlbumEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<FavoriteAlbumEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavorite(favoriteAlbumEntity: FavoriteAlbumEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(favoriteAlbumEntity: FavoriteAlbumEntity) {
        TODO("Not yet implemented")
    }
}