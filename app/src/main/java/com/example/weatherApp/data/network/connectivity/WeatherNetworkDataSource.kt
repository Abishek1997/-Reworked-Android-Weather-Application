package com.example.weatherApp.data.network.connectivity

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.network.pojos.AutocompleteResponse
import com.example.weatherApp.data.network.pojos.CurrentWeatherResponse
import com.example.weatherApp.data.network.pojos.ImagesResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherEntity>
    val downloadedCityImages: LiveData<ImagesResponseEntity>
    val downloadedAutocompleteCities: LiveData<AutocompleteResponse>

    suspend fun fetchCurrentWeather(
       latitude: Double,
       longitude: Double
    )

    suspend fun fetchSearchedWeather(
        city: String
    )

    suspend fun getCityImages(
        city: String
    )

    suspend fun getAutocompleteCities(
        city: String
    )
}