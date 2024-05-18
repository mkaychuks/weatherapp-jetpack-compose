package com.example.weatherapp.di

import com.example.weatherapp.data.WeatherForeCastApi
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesWeatherForecastApi(): WeatherForeCastApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WeatherForeCastApi.BASE_URL)
            .build()
            .create(WeatherForeCastApi::class.java)
    }

    @Provides
    @Singleton
    fun providesWeatherForecastRepository(weatherForecastApi: WeatherForeCastApi): WeatherRepository {
        return WeatherRepositoryImpl(weatherForecastApi)
    }

}