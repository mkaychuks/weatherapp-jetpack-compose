package com.example.weatherapp.domain

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?): Result<T>(data = data)
    class Error<T>(data: T? = null, message: String?): Result<T>(data = data, message = message)
    class Loading<T>(val isLoading: Boolean = true): Result<T>(null)
}