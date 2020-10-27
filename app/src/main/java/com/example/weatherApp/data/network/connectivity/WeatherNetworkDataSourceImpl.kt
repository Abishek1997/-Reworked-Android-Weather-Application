package com.example.weatherApp.data.network.connectivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.network.DarkskyWeatherApiService
import com.example.weatherApp.data.network.pojos.ImagesResponse
import com.example.weatherApp.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val darkskyWeatherApiService: DarkskyWeatherApiService,
): WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherEntity>()
    private val _downloadedCityImages = MutableLiveData<ImagesResponseEntity>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherEntity>
        get() = _downloadedCurrentWeather

    override val downloadedCityImages: LiveData<ImagesResponseEntity>
        get() = _downloadedCityImages

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
            val fetchCurrentWeather = darkskyWeatherApiService.getSearchedWeatherAsync(city)
                .await()
            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun getCityImages(city: String) {
        try {
            val fetchCurrentWeather = darkskyWeatherApiService.getCityImagesAsync(city)
                .await()
            _downloadedCityImages.postValue(fetchCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}