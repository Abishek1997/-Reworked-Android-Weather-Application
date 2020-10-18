package com.example.weatherApp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.LocationEntity

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntity>

    suspend fun getSearchedWeather(city: String)
}