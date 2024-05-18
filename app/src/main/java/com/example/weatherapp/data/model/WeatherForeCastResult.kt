package com.example.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForeCastResult(
    val cityName: String,
    val countryCode: String,
    val data: List<Data>,
    val lat: String,
    val lon: String,
    val stateCode: String,
    val timezone: String
): Parcelable