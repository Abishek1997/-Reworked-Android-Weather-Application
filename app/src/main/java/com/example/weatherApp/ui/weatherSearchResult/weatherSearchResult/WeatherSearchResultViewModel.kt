package com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult


import androidx.lifecycle.ViewModel
import com.example.weatherApp.data.repository.WeatherRepository
import com.example.weatherApp.internal.lazyDeferred

class WeatherSearchResultViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    val currentWeather by lazyDeferred {
        weatherRepository.getCurrentWeather()
    }
}