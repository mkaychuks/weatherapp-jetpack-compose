package com.example.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val appMaxTemp: Double,
    val appMinTemp: Double,
    val clouds: Int,
    val cloudsHi: Int,
    val cloudsLow: Int,
    val cloudsMid: Int,
    val datetime: String,
    val dewpt: Double,
    val highTemp: Double,
    val lowTemp: Double,
    val maxDhi: Double,
    val maxTemp: Double,
    val minTemp: Double,
    val moonPhase: Double,
    val moonPhaseLunation: Double,
    val moonriseTs: Int,
    val moonsetTs: Int,
    val ozone: Double,
    val pop: Int,
    val precip: Double,
    val pres: Double,
    val rh: Int,
    val slp: Double,
    val snow: Int,
    val snowDepth: Int,
    val sunriseTs: Int,
    val sunsetTs: Int,
    val temp: Double,
    val ts: Int,
    val uv: Double,
    val validDate: String,
    val vis: Double,
    val weather: Weather,
    val windCdir: String,
    val windCdirFull: String,
    val windDir: Double,
    val windGustSpd: Double,
    val windSpd: Double
): Parcelable