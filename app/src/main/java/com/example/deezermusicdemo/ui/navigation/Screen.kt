package com.example.deezermusicdemo.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Artist: Screen("artist/{artistId}") {
        fun createRoute(artistId: Long) = "artist/$artistId"
    }
}