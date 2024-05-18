package com.example.weatherapp.domain

import com.example.weatherapp.data.WeatherForeCastApi
import com.example.weatherapp.data.model.WeatherForeCastResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class WeatherRepositoryImpl (
    private val api: WeatherForeCastApi
): WeatherRepository {

    override suspend fun getDailyForeCast(city: String): Flow<Result<WeatherForeCastResult>> {
        return flow {
            val weatherForeCastApiResult = try {
                api.getDailyForeCast(city)
            } catch (e: Exception){
                emit(Result.Error(message = "Error fetching data from server"))
                return@flow
            }
            emit(Result.Success(data = weatherForeCastApiResult))
        }
    }
}