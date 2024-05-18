package com.example.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForeCastResult(
    val city_name: String,
    val country_code: String,
    val data: List<Data>,
    val lat: String,
    val lon: String,
    val state_code: String,
    val timezone: String
): Parcelable