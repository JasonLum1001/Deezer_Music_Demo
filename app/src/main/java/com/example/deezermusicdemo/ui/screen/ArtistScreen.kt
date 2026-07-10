package com.example.deezermusicdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.Heading
import com.example.deezermusicdemo.common.component.IconButton
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.ui.component.ItemView.MusicItemView
import com.example.deezermusicdemo.ui.component.StateView.ErrorStateView
import com.example.deezermusicdemo.ui.component.StateView.LoadingStateView
import com.example.deezermusicdemo.ui.component.StateView.NoNetworkStateView
import com.example.deezermusicdemo.ui.state.ArtistListState
import com.example.deezermusicdemo.ui.viewmodel.ArtistViewModel

@Composable
fun ArtistScreen(
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNavToMusic: (List<MusicItem>, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
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
        ArtistHeader(
            modifier = Modifier.zIndex(1f),
            onBackBtnClicked = onBackBtnClicked,

        )

        when (val state = uiState) {
            is ArtistListState.Loading -> {
                LoadingStateView(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ArtistListState.NetworkError -> {
                NoNetworkStateView(
                    modifier = Modifier.align(Alignment.Center),
                    onRetry = { viewModel.retry() }
                )
            }

            is ArtistListState.Error -> {
                ErrorStateView(
                    modifier = Modifier.align(Alignment.Center),
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }

            is ArtistListState.Success -> {
                SuccessArtistScreen(
                    modifier = Modifier.fillMaxSize(),
                    uiState = state,
                    onNavToMusic = onNavToMusic,
                    onBookmarkClick = { item ->
                        viewModel.toggleBookmark(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun SuccessArtistScreen(
    modifier: Modifier,
    uiState: ArtistListState.Success,
    onNavToMusic: (List<MusicItem>, Int) -> Unit,
    onBookmarkClick: (MusicItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            ArtistInfoBox(
                artistInfo = uiState.artistInfo,
                onPlayClicked = {
                    onNavToMusic.invoke(uiState.artistTracks, 0)
                }
            )
        }

        item {
            Heading(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.artist_top_tracks_title)
            )
        }

        itemsIndexed(
            uiState.artistTracks
        ) { index, item ->
            MusicItemView(
                modifier = Modifier.padding(horizontal = 16.dp),
                item = item,
                onClick = {
                    onNavToMusic.invoke(uiState.artistTracks, index)
                },
                onBookmarkClick = {
                    onBookmarkClick.invoke(item)
                }
            )
        }
    }
}

@Composable
private fun ArtistHeader(
    modifier: Modifier = Modifier,
    onBackBtnClicked: () -> Unit
) {
    // Mask
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.black_40),
                        colorResource(R.color.transparent)
                    )
                )
            )
    ) {
        // Back button
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            iconSize = 60.dp,
            onClick = onBackBtnClicked
        )
    }
}

@Composable
fun ArtistInfoBox(
    artistInfo: ArtistItem,
    onPlayClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {

        // Artist Profile Image
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = artistInfo.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // Mask
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colorResource(R.color.transparent),
                            colorResource(R.color.black_40)
                        )
                    )
                )
        )

        // Artist Name
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            text = artistInfo.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.white_100)
        )

        // Play Button
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 24.dp)
                .padding(end = 16.dp),
            onClick = onPlayClicked,
            shape = CircleShape,
            containerColor = colorResource(R.color.green_100)
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null
            )
        }
    }
}