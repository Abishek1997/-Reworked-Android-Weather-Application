package com.example.weatherApp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.db.entities.LocationEntity
import com.example.weatherApp.data.network.pojos.AutocompleteResponse
import com.example.weatherApp.data.network.pojos.ImagesResponse
import com.example.weatherApp.data.network.pojos.LocationObject

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntity>

    suspend fun getSearchedWeather(city: String)

    suspend fun getWeatherData(): LiveData<out CurrentWeatherEntity>

    suspend fun getCityImages(): LiveData<out ImagesResponseEntity>

    suspend fun getSearchedCityImages(city: String)

    suspend fun getAutocompleteCities(city: String): LiveData<out AutocompleteResponse>

    suspend fun getCurrentFusedLocation(): LiveData<LocationObject>
}