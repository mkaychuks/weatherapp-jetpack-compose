package com.example.weatherapp

import com.example.weatherapp.data.WeatherForeCastApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: WeatherForeCastApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(WeatherForeCastApi.BASE_URL)
        .build()
        .create(WeatherForeCastApi::class.java)
}