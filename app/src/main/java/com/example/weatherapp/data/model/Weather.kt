package com.example.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val code: Int,
    val description: String,
    val icon: String
): Parcelable