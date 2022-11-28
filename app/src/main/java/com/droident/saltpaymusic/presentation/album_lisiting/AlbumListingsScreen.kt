package com.droident.saltpaymusic.presentation.album_lisiting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.droident.saltpaymusic.R
import com.droident.saltpaymusic.ui.modifiers.interceptKey
import com.droident.saltpaymusic.ui.modifiers.rememberContentPaddingForScreen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Destination(start = true)
fun AlbumListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: AlbumListingsViewModel = hiltViewModel(),
) {

    var showTopAppBar by rememberSaveable { mutableStateOf(true) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state
    val topAppBarState = rememberTopAppBarState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            if (showTopAppBar) {
                HomeTopAppBar({
                    showTopAppBar = !showTopAppBar
                }, topAppBarState = topAppBarState)
            }
        },
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier.fillMaxSize(),

                ) {

                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        viewModel.onEvent(AlbumListingsEvent.Refresh)
                    },
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.primaryContainer),
                        contentPadding = rememberContentPaddingForScreen(additionalTop = if (showTopAppBar) 0.dp else 8.dp,
                            excludeTop = showTopAppBar),
                    ) {
                        if (!showTopAppBar) {
                            stickyHeader {
                                HomeSearch(
                                    onSearchInputChanged = {
                                        viewModel.onEvent(AlbumListingsEvent.OnSearchQueryChange(it))
                                    },
                                    showAppTopBar = {
                                        showTopAppBar = !showTopAppBar
                                    },
                                    modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
                                    searchInput = state.searchQuery)
                            }
                        }
                        items(state.albums.size) { i ->
                            val album = state.albums[i]
                            AlbumItem(isFavorite = state.favorites.contains(album.albumId),
                                album = album,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onToggleFavorite = { albumId, isFavourite ->
                                    viewModel.onEvent(AlbumListingsEvent.toggleFavourite(albumId,
                                        isFavourite))
                                }

                            )
                            if (i < state.albums.size) {
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
            if (state.error.isNotBlank()) {
                Text(text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center))
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun HomeSearch(
    showAppTopBar: () -> Unit,
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(top = 8.dp, bottom = 8.dp, start = 14.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            /* TODO: Open search */
            showAppTopBar()
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                containerColor= White,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            ),
            shape = CircleShape,
            value = searchInput,
            onValueChange = onSearchInputChanged,
            placeholder = { Text(stringResource(R.string.home_search)) },
            leadingIcon = { Icon(Icons.Filled.Search, null) },
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .interceptKey(Key.Enter) {
                    onSearchInputChanged(searchInput)
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                },
            singleLine = true,
            // keyboardOptions change the newline key to a search key on the soft keyboard
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            // keyboardActions submits the search query when the search key is pressed
            keyboardActions = KeyboardActions(onSearch = {

                onSearchInputChanged(searchInput)
                keyboardController?.hide()
            }))
    }

}

/**
 * TopAppBar for the Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    onClickSearch: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState),
) {
    val title = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(title = {
        Text(text = "SaltPay Music")
    },

        actions = {
            IconButton(onClick = {
                /* TODO: Open search */
                onClickSearch()
            }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
        }, scrollBehavior = scrollBehavior, modifier = modifier)
}
