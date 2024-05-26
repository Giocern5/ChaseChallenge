package com.example.chasechallenge.ui.screen

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chasechallenge.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.WeatherSearchScreen.route,
        modifier = Modifier.background(Color.DarkGray)
    ) {
        composable(route = Screen.WeatherSearchScreen.route) {
            WeatherSearchScreen()
        }
    }
}