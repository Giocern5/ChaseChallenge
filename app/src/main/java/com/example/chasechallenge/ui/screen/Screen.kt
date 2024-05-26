package com.example.chasechallenge.ui.screen

sealed class Screen(val route: String) {
    object WeatherSearchScreen : Screen("WeatherSearchScreen")
}