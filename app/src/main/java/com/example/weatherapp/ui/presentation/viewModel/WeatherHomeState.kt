package com.example.weatherapp.ui.presentation.viewModel

import com.example.weatherapp.data.model.WeatherForeCastResult

data class WeatherHomeState(
    val isLoading: Boolean = false,
    val data: WeatherForeCastResult? = null
)



sealed class WeatherHomeUiEvent{
    data class SearchIconPressed(val city: String): WeatherHomeUiEvent()
}