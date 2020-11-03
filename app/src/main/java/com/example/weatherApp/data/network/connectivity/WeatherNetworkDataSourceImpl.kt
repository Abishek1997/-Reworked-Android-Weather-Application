package com.example.weatherApp.data.network.connectivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.network.DarkskyWeatherApiService
import com.example.weatherApp.data.network.pojos.AutocompleteResponse
import com.example.weatherApp.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val darkskyWeatherApiService: DarkskyWeatherApiService,
): WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherEntity>()
    private val _downloadedCityImages = MutableLiveData<ImagesResponseEntity>()
    private val _downloadedAutocompleteCities = MutableLiveData<AutocompleteResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherEntity>
        get() = _downloadedCurrentWeather

    override val downloadedCityImages: LiveData<ImagesResponseEntity>
        get() = _downloadedCityImages

    override val downloadedAutocompleteCities: LiveData<AutocompleteResponse>
        get() = _downloadedAutocompleteCities

    override suspend fun fetchCurrentWeather(latitude: Double, longitude: Double) {
        try {
            val fetchCurrentWeather = darkskyWeatherApiService.getCurrentWeatherAsync(latitude, longitude)
                .await()
            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection", e)
        }
    }
    override suspend fun fetchSearchedWeather(city: String) {
        try {
            val fetchSearchedWeather = darkskyWeatherApiService.getSearchedWeatherAsync(city)
                .await()
            _downloadedCurrentWeather.postValue(fetchSearchedWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun getCityImages(city: String) {
        try {
            val fetchCityImages = darkskyWeatherApiService.getCityImagesAsync(city)
                .await()
            _downloadedCityImages.postValue(fetchCityImages)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun getAutocompleteCities(city: String) {
        try {
            val fetchAutocompleteCities = darkskyWeatherApiService.getAutocompleteCitiesAsync(city)
                .await()
            _downloadedAutocompleteCities.postValue(fetchAutocompleteCities)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}