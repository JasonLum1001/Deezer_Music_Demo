package com.example.deezermusicdemo.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.Heading
import com.example.deezermusicdemo.common.component.IconButton
import com.example.deezermusicdemo.common.component.TitleBar
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.ui.component.ItemView.ArtistItemView
import com.example.deezermusicdemo.ui.component.ItemView.MusicItemView
import com.example.deezermusicdemo.ui.component.StateView.ErrorStateView
import com.example.deezermusicdemo.ui.component.StateView.LoadingStateView
import com.example.deezermusicdemo.ui.component.StateView.NoNetworkStateView
import com.example.deezermusicdemo.ui.state.HomeListState
import com.example.deezermusicdemo.ui.viewmodel.MusicViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicViewModel = hiltViewModel(),
    onNavToArtist: (Long) -> Unit,
    onNavToMusic: (List<MusicItem>, Int) -> Unit,
    onNavToBookmark: () -> Unit,
    onNavToSearch: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.transparent),
                        colorResource(R.color.green_20)
                    )
                )
            )
    ) {
        HomeHeader(
            onNavToBookmark = onNavToBookmark,
            onNavToSearch = onNavToSearch
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = uiState) {
                is HomeListState.Loading -> {
                    LoadingStateView(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is HomeListState.NetworkError -> {
                    NoNetworkStateView(
                        modifier = Modifier.align(Alignment.Center),
                        onRetry = { viewModel.retry() }
                    )
                }

                is HomeListState.Error -> {
                    ErrorStateView(
                        modifier = Modifier.align(Alignment.Center),
                        message = state.message,
                        onRetry = { viewModel.retry() }
                    )
                }

                is HomeListState.Success -> {
                    SuccessHomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        uiState = state,
                        onNavToArtist = onNavToArtist,
                        onNavToMusic = onNavToMusic,
                        onBookmarkClick = { item ->
                            viewModel.toggleBookmark(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    onNavToBookmark: () -> Unit,
    onNavToSearch: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleBar(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.app_title)
        )

        // Bookmark Entry Button
        IconButton(
            modifier = Modifier.padding(top = 16.dp),
            imageVector = Icons.Filled.Bookmark,
            iconSize = 32.dp,
            onClick = onNavToBookmark
        )

        // Search Button
        IconButton(
            modifier = Modifier.padding(top = 16.dp),
            imageVector = Icons.Filled.Search,
            iconSize = 32.dp,
            onClick = onNavToSearch
        )
    }
}

@Composable
private fun SuccessHomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeListState.Success,
    onNavToArtist: (Long) -> Unit,
    onNavToMusic: (List<MusicItem>, Int) -> Unit,
    onBookmarkClick: (MusicItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Heading(
                text = stringResource(R.string.artist_list_title)
            )
        }

        item {
            ArtistListView(
                artistList = uiState.recommendedArtist,
                onItemClick = { item ->
                    Log.d("HomeScreen", "Artist: ${item.name} clicked")
                    onNavToArtist.invoke(item.id)
                }
            )
        }

        item {
            Heading(
                text = stringResource(R.string.music_list_title)
            )
        }

        itemsIndexed(
            items = uiState.recommendedMusic,
            itemContent = { index, item ->
                MusicItemView(
                    item = item,
                    onClick = {
                        Log.d("HomeScreen", "Music: ${item.title} clicked")
                        onNavToMusic.invoke(uiState.recommendedMusic, index)
                    },
                    onBookmarkClick = {
                        onBookmarkClick.invoke(item)
                    }
                )
            }
        )

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun ArtistListView(
    modifier: Modifier = Modifier,
    artistList: List<ArtistItem>,
    onItemClick: (ArtistItem) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = artistList,
            itemContent = { item ->
                ArtistItemView(
                    item = item,
                    onClick = { onItemClick.invoke(item)}
                )
            }
        )
    }
}

