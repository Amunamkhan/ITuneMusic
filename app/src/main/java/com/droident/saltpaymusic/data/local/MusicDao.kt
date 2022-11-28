package com.droident.saltpaymusic.data.local

import androidx.room.*

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(
        albumListingEntities: List<AlbumEntity>,
    )
    // Clear datbase for to refresh data
    @Query("DELETE FROM albumentity")
    suspend fun clearAlbums()
    @Query("""
            SELECT * 
            FROM albumentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
               LOWER(author) LIKE '%' || LOWER(:query)|| '%' 
        """)
    suspend fun searchAlbums(query: String): List<AlbumEntity>

    /**
     * Get all favorite albums from the albumentity table.
     */
    //Todo Used to show for favorite table screen
    @Query("SELECT * FROM albumentity WHERE id IN (:albumIds)")
    suspend fun getFavoriteAlbums(albumIds: List<Int>): List<AlbumEntity>
    // getting favorites ids
    @Query("SELECT * FROM favorite_albums")
    suspend fun getFavorites(): List<FavoriteAlbumEntity>

    // add  favorites on the basis of album ids
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteAlbumEntity: FavoriteAlbumEntity)

    // remove  favorites on the basis of album ids
    @Delete
    suspend  fun removeFavorite(favoriteAlbumEntity: FavoriteAlbumEntity)
}