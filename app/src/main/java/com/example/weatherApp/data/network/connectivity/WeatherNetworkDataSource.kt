package com.example.weatherApp.data.network.connectivity

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.network.pojos.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherEntity>

    suspend fun fetchCurrentWeather(
       latitude: Double,
       longitude: Double
    )

    suspend fun fetchSearchedWeather(
        city: String
    )
}