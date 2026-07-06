package com.example.deezermusicdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deezermusicdemo.ui.navigation.Screen
import com.example.deezermusicdemo.ui.screen.ArtistScreen
import com.example.deezermusicdemo.ui.screen.HomeScreen
import com.example.deezermusicdemo.ui.screen.SearchScreen
import com.example.deezermusicdemo.ui.theme.DeezerMusicDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeezerMusicDemoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
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
                    onNavToSearch = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }

            composable(
                route = Screen.Search.route
            ) {
                SearchScreen(
                    onBackBtnClicked = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Artist.route,
                arguments = listOf(
                    navArgument("artistId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                ArtistScreen(
                    onBackBtnClicked = { navController.popBackStack() }
                )
            }
        }
    }
}