package com.example.deezermusicdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.ui.navigation.Screen
import com.example.deezermusicdemo.ui.viewmodel.PlayerViewModel


@Composable
fun MainScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                listOf(
                    colorResource(R.color.transparent),
                    colorResource(R.color.green_20)
                )
            )
        )
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screen.Home.route
            ) {
                HomeScreen(
                    onNavToArtist = { artistId ->
                        navController.navigate(Screen.Artist.createRoute(artistId))
                    },
                    onNavToMusic = {musicItems, index ->
                        viewModel.playPlaylist(musicItems, index)
                    },
                    onNavToSearch = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }

            composable(
                route = Screen.Search.route
            ) {
                SearchScreen(
                    onBackBtnClicked = {
                        navController.popBackStack()
                    },
                    onNavToMusic = {musicItems, index ->
                        viewModel.playPlaylist(musicItems, index)
                    }
                )
            }

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
                    onNavToMusic = {musicItems, index ->
                        viewModel.playPlaylist(musicItems, index)
                    }
                )
            }
        }
    }

    // Bottom Sheet

}