package com.droident.saltpaymusic.presentation.album_lisiting

import com.droident.saltpaymusic.domain.model.Album


data class AlbumListingsState(
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false,
    val error:String="",
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    var favorites: Set<Int> = emptySet(),
)
