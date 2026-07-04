package com.example.deezermusicdemo.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.Heading
import com.example.deezermusicdemo.common.component.TitleBar
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.ui.component.ArtistItemView
import com.example.deezermusicdemo.ui.component.MusicItemView
import com.example.deezermusicdemo.ui.viewmodel.MusicViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicViewModel = hiltViewModel()
) {
    val recommendedMusic by viewModel.recommendedMusic.collectAsState()
    val recommendedArtist by viewModel.recommendedArtist.collectAsState()

    LazyColumn(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            TitleBar(
                text = stringResource(R.string.app_title)
            )
        }

        item {
            Heading(
                text = stringResource(R.string.artist_list_title)
            )
        }

        item {
            ArtistListView(
                artistList = recommendedArtist,
                onItemClick = { item ->
                    Log.d("HomeScreen", "Artist: ${item.name} clicked")
                }
            )
        }

        item {
            Heading(
                text = stringResource(R.string.music_list_title)
            )
        }

        items(
            items = recommendedMusic,
            itemContent = { item ->
                MusicItemView(
                    item = item,
                    onClick = {
                        Log.d("HomeScreen", "Music: ${item.title} clicked")
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

