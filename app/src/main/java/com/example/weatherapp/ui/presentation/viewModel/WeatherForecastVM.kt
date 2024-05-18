package com.example.weatherapp.ui.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.Result
import com.example.weatherapp.domain.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherForecastVM @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {
    private val tag = "WEATHER API"


    // getting the state of the app screen
    private var _weatherForeCastState = MutableStateFlow(WeatherHomeState())
    val weatherForeCastState = _weatherForeCastState.asStateFlow()

    init {
        viewModelScope.launch {
            weatherRepository.getDailyForeCast("Onitsha").collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        Log.d(tag, "Error fetching the weather details of the app")
                    }

                    is Result.Loading -> {
                        _weatherForeCastState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                        Log.d(
                            tag,
                            "Loading Screen: The weather api forecast is still in background"
                        )
                    }

                    is Result.Success -> {
                        _weatherForeCastState.update {
                            it.copy(
                                isLoading = false,
                                data = result.data!!
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: WeatherHomeUiEvent) {
        when (event) {
            is WeatherHomeUiEvent.SearchIconPressed -> {
                viewModelScope.launch {
                    weatherRepository.getDailyForeCast(event.city).collectLatest { result ->
                        when (result) {
                            is Result.Error -> {
                                Log.d(tag, "Error fetching the weather details of the app")
                            }

                            is Result.Loading -> {
                                _weatherForeCastState.update {
                                    it.copy(
                                        isLoading = true
                                    )
                                }
                            }

                            is Result.Success -> {
                                _weatherForeCastState.update {
                                    it.copy(
                                        isLoading = false,
                                        data = result.data!!
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}