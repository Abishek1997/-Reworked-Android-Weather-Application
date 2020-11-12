package com.example.weatherApp.ui.weatherSearchResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherApp.data.network.connectivity.ConnectivityInterceptor
import com.example.weatherApp.data.network.pojos.AutocompleteResponse
import com.example.weatherApp.data.repository.WeatherRepository

class WeatherSearchActivityViewModel(
    private val weatherRepository: WeatherRepository,
    private val connectivityInterceptor: ConnectivityInterceptor
): ViewModel() {
    suspend fun setQueryToRepository(city: String){
        weatherRepository.getSearchedWeather(city)
        weatherRepository.getSearchedCityImages(city)
    }

    suspend fun getAutocompleteCitiesByQuery(city: String): LiveData<out AutocompleteResponse>{
        return weatherRepository.getAutocompleteCities(city)
    }

    fun checkInternetConnection(): Boolean {
        return connectivityInterceptor.isOnline()
    }
}