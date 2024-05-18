package com.example.weatherapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.model.WeatherForeCastResult
import com.example.weatherapp.ui.presentation.screens.WeatherDetailScreen
import com.example.weatherapp.ui.presentation.screens.WeatherHomeScreen

sealed class Screen(val route: String) {
    data object WeatherHomeScreen: Screen(route = "Home")
    data object WeatherDetailScreen: Screen(route = "WeatherDetails")
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.WeatherHomeScreen.route) {
        composable(
            Screen.WeatherHomeScreen.route,
        ){
            WeatherHomeScreen(
                navigateToDetailsScreen = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("data", it)
                    navController.navigate(Screen.WeatherDetailScreen.route)
                }
            )
        }
        composable(
            Screen.WeatherDetailScreen.route,
        ){
            navController.previousBackStackEntry?.savedStateHandle?.get<WeatherForeCastResult>("data")?.let {
                WeatherDetailScreen(
                    weatherForeCastResult = it,
                    onNavigationBackButtonPressed = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            "route"
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Hello")
            }
        }
    }
}