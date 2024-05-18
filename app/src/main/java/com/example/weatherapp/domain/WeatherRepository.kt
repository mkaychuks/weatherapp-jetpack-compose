package com.example.weatherapp.domain

import com.example.weatherapp.data.model.WeatherForeCastResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getDailyForeCast(city : String): Flow<Result<WeatherForeCastResult>>
}