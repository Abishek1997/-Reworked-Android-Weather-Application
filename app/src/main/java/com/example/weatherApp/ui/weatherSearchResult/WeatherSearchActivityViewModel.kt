package com.example.weatherApp.ui.weatherSearchResult

import androidx.lifecycle.ViewModel
import com.example.weatherApp.data.repository.WeatherRepository

class WeatherSearchActivityViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {
    suspend fun setQueryToRepository(city: String){
        weatherRepository.getSearchedWeather(city)
    }
}