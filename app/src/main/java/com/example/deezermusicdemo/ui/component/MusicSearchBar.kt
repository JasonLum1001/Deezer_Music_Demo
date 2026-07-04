package com.example.deezermusicdemo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.domain.model.MusicItem
import kotlin.collections.forEach

@Composable
fun MusicSearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        SearchBarView(
            modifier = Modifier,
            query = searchQuery,
            onQueryChange = { query ->
                expanded = query.isNotBlank()
                searchQuery = query
            },
            onSearch = {
                expanded = false
                onSearch.invoke(searchQuery)
            }
        )

        // TODO: Suggestion logic
        SearchDropdown(
            modifier = Modifier,
            expanded = expanded,
            suggestions = listOf(),
            onDismissRequest = {
                expanded = false
            },
            onSuggestionClick = { track ->
                expanded = false
            }
        )
    }
}

@Composable
fun SearchBarView(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                colorResource(R.color.white_100),
                RoundedCornerShape(30.dp)
            )
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Search songs, artists, podcast")
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparent),
                unfocusedContainerColor = colorResource(R.color.transparent),
                focusedIndicatorColor = colorResource(R.color.transparent),
                unfocusedIndicatorColor = colorResource(R.color.transparent)
            )
        )

        IconButton(
            onClick = onSearch
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}

@Composable
fun SearchDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    suggestions: List<MusicItem>,
    onDismissRequest: () -> Unit,
    onSuggestionClick: (MusicItem) -> Unit,
) {
    DropdownMenu(
        expanded = expanded && suggestions.isNotEmpty(),
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        suggestions.forEach { suggestion ->
            MusicItemView(
                item = suggestion,
                onClick = {
                    onSuggestionClick.invoke(suggestion)
                }
            )
        }
    }
}