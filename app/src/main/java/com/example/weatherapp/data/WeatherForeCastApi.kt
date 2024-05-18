package com.example.weatherapp.data

import com.example.weatherapp.data.model.WeatherForeCastResult
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForeCastApi {

    @GET("daily")
    suspend fun getDailyForeCast(
        @Query("city") city : String,
        @Query("key") key: String = API_KEY
    ): WeatherForeCastResult

    companion object{
        const val BASE_URL = "https://api.weatherbit.io/v2.0/forecast/"
        const val API_KEY = "a7a6face31024453820db52e0abfda41"
    }
}