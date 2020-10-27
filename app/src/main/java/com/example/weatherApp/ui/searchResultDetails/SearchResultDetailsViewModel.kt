package com.example.weatherApp.ui.searchResultDetails

import androidx.lifecycle.ViewModel
import com.example.weatherApp.data.repository.WeatherRepository
import com.example.weatherApp.internal.lazyDeferred

class SearchResultDetailsViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {
    val currentWeather by lazyDeferred {
        weatherRepository.getWeatherData()
    }
    val cityImages by lazyDeferred {
        weatherRepository.getCityImages()
    }
}