package com.example.weatherApp.ui.weatherSearchResult.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherApp.data.repository.WeatherRepository
import com.example.weatherApp.ui.weatherSearchResult.WeatherSearchActivityViewModel

@Suppress("UNCHECKED_CAST")
class FavoritesViewModelFactory (
    private val weatherRepository : WeatherRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(weatherRepository) as T
    }
}