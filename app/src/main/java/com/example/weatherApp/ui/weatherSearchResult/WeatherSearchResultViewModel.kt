package com.example.weatherApp.ui.weatherSearchResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.repository.WeatherRepository
import com.example.weatherApp.internal.lazyDeferred
import kotlinx.coroutines.Deferred

class WeatherSearchResultViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    val currentWeather by lazyDeferred {
        weatherRepository.getCurrentWeather()
    }
}