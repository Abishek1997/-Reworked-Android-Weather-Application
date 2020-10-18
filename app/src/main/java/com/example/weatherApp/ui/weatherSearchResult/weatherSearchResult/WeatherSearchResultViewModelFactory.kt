package com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherApp.data.repository.WeatherRepository

@Suppress("UNCHECKED_CAST")
class WeatherSearchResultViewModelFactory(
    private val weatherRepository : WeatherRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherSearchResultViewModel(weatherRepository) as T
    }
}