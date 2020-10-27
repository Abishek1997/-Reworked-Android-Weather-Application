package com.example.weatherApp.ui.searchResultDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherApp.data.repository.WeatherRepository

@Suppress("UNCHECKED_CAST")
class SearchResultDetailsViewModelFactory(
    private val weatherRepository : WeatherRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchResultDetailsViewModel(weatherRepository) as T
    }
}