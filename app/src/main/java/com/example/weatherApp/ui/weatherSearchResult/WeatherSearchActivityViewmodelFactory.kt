package com.example.weatherApp.ui.weatherSearchResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherApp.data.network.connectivity.ConnectivityInterceptor
import com.example.weatherApp.data.repository.WeatherRepository

@Suppress("UNCHECKED_CAST")
class WeatherSearchActivityViewmodelFactory (
    private val weatherRepository : WeatherRepository,
    private val connectivityInterceptor: ConnectivityInterceptor
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherSearchActivityViewModel(weatherRepository, connectivityInterceptor) as T
    }
}