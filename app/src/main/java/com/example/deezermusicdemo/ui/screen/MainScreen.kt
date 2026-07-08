package com.example.deezermusicdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.domain.model.MusicItem
import com.example.deezermusicdemo.ui.component.widget.MiniPlayer
import com.example.deezermusicdemo.ui.component.widget.PlayerBottomSheet
import com.example.deezermusicdemo.ui.navigation.Screen
import com.example.deezermusicdemo.ui.viewmodel.PlayerViewModel


@Composable
fun MainScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val state by viewModel.state.collectAsState()
    var showPlayerSheet by remember { mutableStateOf(false) }

    fun playMusic(
        musicItems: List<MusicItem>,
        index: Int
    ) {
        viewModel.playPlaylist(musicItems, index)
        showPlayerSheet = true
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.transparent),
                        colorResource(R.color.green_20)
                    )
                )
            ),
        bottomBar = {
            if (state.currentSong != null) {
                MiniPlayer(
                    modifier = Modifier.padding(bottom = 50.dp),
                    item = state.currentSong!!,
                    isPlaying = state.isPlaying,
                    onPlayPause = { viewModel.playPause() },
                    onSkipPrevious = { viewModel.skipPrevious() },
                    onSkipNext = { viewModel.skipNext() },
                    onClick = { showPlayerSheet = true }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            // Home Page
            composable(
                route = Screen.Home.route
            ) {
                HomeScreen(
                    onNavToArtist = { artistId ->
                        navController.navigate(Screen.Artist.createRoute(artistId))
                    },
                    onNavToMusic = { musicItems, index ->
                        playMusic(musicItems, index)
                    },
                    onNavToSearch = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }

            // Search Page
            composable(
                route = Screen.Search.route
            ) {
                SearchScreen(
                    onBackBtnClicked = {
                        navController.popBackStack()
                    },
                    onNavToMusic = { musicItems, index ->
                        playMusic(musicItems, index)
                    }
                )
            }

            // Artist Page
            composable(
                route = Screen.Artist.route,
                arguments = listOf(
                    navArgument("artistId") {
                        type = NavType.LongType
                    }
                )
            ) { _ ->
                ArtistScreen(
                    onBackBtnClicked = {
                        navController.popBackStack()
                    },
                    onNavToMusic = { musicItems, index ->
                        playMusic(musicItems, index)
                    }
                )
            }
        }
    }

    // Bottom Sheet
    if (state.currentSong != null && showPlayerSheet) {
        PlayerBottomSheet(
            playerStateFlow = viewModel.state,
            onSeek = { position ->
                viewModel.seekTo(position.toLong())
            },
            onPlayPause = {
                viewModel.playPause()
            },
            onSkipNext = {
                viewModel.skipNext()
            },
            onSkipPrevious = {
                viewModel.skipPrevious()
            },
            onToggleRepeat = {
                viewModel.toggleRepeatMode()
            },
            onToggleShuffle = {
                viewModel.toggleShuffleMode()
            },
            onDismiss = {
                showPlayerSheet = false
            }
        )
    }
}