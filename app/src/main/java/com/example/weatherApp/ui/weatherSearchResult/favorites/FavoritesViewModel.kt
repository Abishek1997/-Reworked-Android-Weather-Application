package com.example.weatherApp.ui.weatherSearchResult.favorites

import androidx.lifecycle.ViewModel
import com.example.weatherApp.data.repository.WeatherRepository

class FavoritesViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    suspend fun setQueryToRepository(city: String){
        weatherRepository.getSearchedWeather(city)
        weatherRepository.getSearchedCityImages(city)
    }
}