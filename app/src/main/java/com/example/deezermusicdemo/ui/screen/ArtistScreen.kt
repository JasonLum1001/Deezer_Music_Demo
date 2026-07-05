package com.example.deezermusicdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.Heading
import com.example.deezermusicdemo.domain.model.ArtistItem
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.ui.component.MusicItemView
import com.example.deezermusicdemo.ui.viewmodel.ArtistViewModel

@Composable
fun ArtistScreen(
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit
) {
    val artistInfo by viewModel.artistInfo.collectAsState()
    val artistTracks by viewModel.artistTracks.collectAsState()

    LazyColumn(
        modifier = modifier
            .background(
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
            ArtistHeader(
                artistInfo = artistInfo,
                onBackBtnClicked = onBackBtnClicked
            )
        }

        item {
            Heading(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.artist_top_tracks_title)
            )
        }

        items(artistTracks) { item ->
            MusicItemView(
                modifier = Modifier.padding(horizontal = 16.dp),
                item = item,
                onClick = {}
            )
        }
    }
}

@Composable
fun ArtistHeader(
    artistInfo: ArtistItem?,
    onBackBtnClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {

        if (artistInfo != null) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = artistInfo.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colorResource(R.color.black_40),
                            colorResource(R.color.transparent),
                            colorResource(R.color.black_40)
                        )
                    )
                )
        )

        IconButton(
            onClick = onBackBtnClicked,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null
            )
        }

        if (artistInfo != null) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                text = artistInfo.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white_100)
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = 24.dp)
                    .padding(end = 16.dp),
                onClick = {},
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
}