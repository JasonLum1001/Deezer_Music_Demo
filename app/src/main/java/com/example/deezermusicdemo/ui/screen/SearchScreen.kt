package com.example.deezermusicdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.IconButton
import com.example.deezermusicdemo.ui.component.MusicItemView
import com.example.deezermusicdemo.ui.component.MusicSearchBar
import com.example.deezermusicdemo.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit
) {
    val searchResults by viewModel.searchResults.collectAsState()

    LazyColumn(
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    colorResource(R.color.transparent),
                    colorResource(R.color.green_20)
                )
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            SearchHeader(
                onBackBtnClicked = onBackBtnClicked,
                onSearch = { query -> viewModel.searchTracks(query) }
            )
        }

        items(
            items = searchResults
        ) { item ->
            MusicItemView(
                item = item,
                onClick = {}
            )
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
            placeholderText = "Search songs, artists, podcast",
            fontSize = 16.sp,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.green_100),
                                shape = CircleShape
                            )
                            .size(32.dp)
                            .padding(4.dp),
                        imageVector = Icons.Default.Search,
                        onClick = {
                            onSearch.invoke(searchQuery)
                            searchQuery = ""
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        )
    }

}