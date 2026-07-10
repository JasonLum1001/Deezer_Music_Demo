package com.example.deezermusicdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.IconButton
import com.example.deezermusicdemo.common.component.TitleBar
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.ui.component.ItemView.MusicItemView
import com.example.deezermusicdemo.ui.component.StateView.ErrorStateView
import com.example.deezermusicdemo.ui.component.StateView.LoadingStateView
import com.example.deezermusicdemo.ui.component.StateView.NoNetworkStateView
import com.example.deezermusicdemo.ui.state.ArtistListState
import com.example.deezermusicdemo.ui.state.BookmarkListState
import com.example.deezermusicdemo.ui.viewmodel.ArtistViewModel
import com.example.deezermusicdemo.ui.viewmodel.BookmarkViewModel

@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNavToMusic: (List<MusicItem>, Int) -> Unit
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
        BookmarkHeader(
            modifier = Modifier.zIndex(1f),
            onBackBtnClicked = onBackBtnClicked,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = uiState) {
                is BookmarkListState.Loading -> {
                    LoadingStateView(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is BookmarkListState.Error -> {
                    ErrorStateView(
                        modifier = Modifier.align(Alignment.Center),
                        message = state.message,
                        onRetry = null
                    )
                }

                is BookmarkListState.Empty -> {
                    ErrorStateView(
                        modifier = Modifier.align(Alignment.Center),
                        message = stringResource(R.string.empty_bookmark_msg),
                        onRetry = null
                    )
                }

                is BookmarkListState.Success -> {
                    SuccessBookmarkScreen(
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
}

@Composable
private fun SuccessBookmarkScreen(
    modifier: Modifier,
    uiState: BookmarkListState.Success,
    onNavToMusic: (List<MusicItem>, Int) -> Unit,
    onBookmarkClick: (MusicItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(
            uiState.bookmarkList
        ) { index, item ->
            MusicItemView(
                modifier = Modifier.padding(horizontal = 16.dp),
                item = item,
                onClick = {
                    onNavToMusic.invoke(uiState.bookmarkList, index)
                },
                onBookmarkClick = {
                    onBookmarkClick.invoke(item)
                }
            )
        }
    }
}

@Composable
private fun BookmarkHeader(
    modifier: Modifier = Modifier,
    onBackBtnClicked: () -> Unit
) {
    // Mask
    Row(
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
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        IconButton(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            iconSize = 60.dp,
            onClick = onBackBtnClicked
        )

        TitleBar(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.bookmark_list_title),
        )
    }
}