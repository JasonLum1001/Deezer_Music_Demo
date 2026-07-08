package com.example.deezermusicdemo.ui.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.IconButton
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.ui.component.ItemView.MusicItemView
import com.example.deezermusicdemo.ui.component.StateView.ErrorStateView
import com.example.deezermusicdemo.ui.component.StateView.LoadingStateView
import com.example.deezermusicdemo.ui.component.StateView.NoNetworkStateView
import com.example.deezermusicdemo.ui.component.widget.MusicSearchBar
import com.example.deezermusicdemo.ui.state.SearchListState
import com.example.deezermusicdemo.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
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
        SearchHeader(
            onBackBtnClicked = onBackBtnClicked,
            onSearch = { query ->
                viewModel.searchTracks(query)
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = uiState) {
                is SearchListState.Loading -> {
                    LoadingStateView(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is SearchListState.NetworkError -> {
                    NoNetworkStateView(
                        modifier = Modifier.align(Alignment.Center),
                        onRetry = { viewModel.retry() }
                    )
                }

                is SearchListState.Error -> {
                    ErrorStateView(
                        modifier = Modifier.align(Alignment.Center),
                        message = state.message,
                        onRetry = { viewModel.retry() }
                    )
                }

                is SearchListState.Success -> {
                    SuccessSearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        uiState = state,
                        onNavToMusic = onNavToMusic
                    )
                }
            }
        }

    }
}

@Composable
fun SearchHeader(
    onBackBtnClicked: () -> Unit,
    onSearch: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by remember { mutableStateOf("") }

    fun search() {
        onSearch.invoke(searchQuery)
        searchQuery = ""
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Back Button
        IconButton(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            iconSize = 60.dp,
            onClick = onBackBtnClicked
        )

        // Search Bar
        MusicSearchBar(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(30.dp)),
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
            },
            placeholderText = stringResource(R.string.search_bar_hint),
            fontSize = 16.sp,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { search() }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.green_100),
                                shape = CircleShape
                            )
                            .size(32.dp)
                            .padding(4.dp),
                        imageVector = Icons.Default.Search,
                        onClick = { search() }
                    )
                }
            }
        )
    }

}

@Composable
fun SuccessSearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchListState.Success,
    onNavToMusic: (List<MusicItem>, Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        itemsIndexed(
            items = uiState.searchResult
        ) { index, item ->
            MusicItemView(
                item = item,
                onClick = {
                    onNavToMusic.invoke(listOf(item), index)
                }
            )
        }
    }
}