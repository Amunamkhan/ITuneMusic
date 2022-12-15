package com.droident.saltpaymusic.presentation.album_lisiting


sealed class AlbumListingsEvent {
    // handle swipe to refresh functionality
    object Refresh: AlbumListingsEvent()
    // handle search query
    data class OnSearchQueryChange(val query: String): AlbumListingsEvent()
    // handle favorite functionality
    data class toggleFavourite(val albumId: Int,val isFavourite: Boolean): AlbumListingsEvent()

}
