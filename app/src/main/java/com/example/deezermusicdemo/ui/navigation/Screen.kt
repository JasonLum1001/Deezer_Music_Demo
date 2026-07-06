package com.example.deezermusicdemo.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Search: Screen("search")
    object Artist: Screen("artist/{artistId}") {
        fun createRoute(artistId: Long) = "artist/$artistId"
    }
}