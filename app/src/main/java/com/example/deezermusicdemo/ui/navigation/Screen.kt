package com.example.deezermusicdemo.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
}