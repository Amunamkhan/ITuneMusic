package com.droident.saltpaymusic.presentation.album_lisiting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droident.saltpaymusic.domain.repository.MusicRepository
import com.droident.saltpaymusic.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListingsViewModel @Inject constructor(
    private val repository: MusicRepository,
) : ViewModel() {

    var state by mutableStateOf(AlbumListingsState())

    private var searchJob: Job? = null

    init {
        // First time album loads
        getAlbumListings()
        // Here we are getting favorite list
        observeFavorites()
    }

    /**
     * Here we are handling all the event occur on the basis User interaction
     */

    fun onEvent(event: AlbumListingsEvent) {
        when (event) {
            is AlbumListingsEvent.Refresh -> {
                getAlbumListings(fetchFromRemote = true)
            }
            is AlbumListingsEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getAlbumListings()
                }
            }

            is AlbumListingsEvent.toggleFavourite -> {
                viewModelScope.launch {
                    repository.addOrRemoveAlbumToFavorite(event.albumId, event.isFavourite)
                     observeFavorites()
                }
            }
        }
    }

    /**
     * Getting the data from data layer and displaying on ui
     */
    private fun getAlbumListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false,
    ) {
        viewModelScope.launch {
            repository.getAlbums(fetchFromRemote, query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { listings ->
                            state = state.copy(albums = listings)
                        }
                    }
                    is Resource.Error -> {
                        state=state.copy(error = result.message?:"Some thing went wrong")
                    }
                    is Resource.Loading -> {
                        state = state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    /**
     * Here we are getting the favorite from data layer
     */
    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { favorites ->
                            state = state.copy(favorites = favorites)
                        }
                    }

                }
            }
        }
    }
}