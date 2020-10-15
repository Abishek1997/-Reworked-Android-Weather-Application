package com.example.weatherApp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntity>
}