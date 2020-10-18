package com.example.weatherApp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.daos.CurrentWeatherDAO
import com.example.weatherApp.data.db.daos.WeatherLocationDAO
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.LocationEntity
import com.example.weatherApp.data.network.connectivity.LocationProvider
import com.example.weatherApp.data.network.connectivity.WeatherNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class WeatherRepositoryImpl(
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {

    init{
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever{ newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }

    }
    override suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntity> {
        return withContext(Dispatchers.IO) {
            initFetchWeatherData()
            return@withContext currentWeatherDAO.getCurrentWeatherFromDB()
        }
    }

    override suspend fun getSearchedWeather(city: String) {
        return withContext(Dispatchers.IO) {
            initFetchSearchedWeatherData(city)
        }
    }

    private suspend fun initFetchSearchedWeatherData(city: String){
        //TODO: Check if fetch is needed
        fetchSearchedWeather(city)
    }

    private suspend fun fetchSearchedWeather(city: String){
        weatherNetworkDataSource.fetchSearchedWeather(city)
    }
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherEntity){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDAO.upsertCurrentWeatherData(fetchedWeather)
        }
    }

    private suspend fun initFetchWeatherData(){
        if (isFetchNeeded(ZonedDateTime.now().minusMinutes(10)))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            34.04563903808594,
            -118.24163818359375

        )
    }

    private fun isFetchNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val fiveMinutesAgo = ZonedDateTime.now().minusMinutes(5)
        return lastFetchTime.isBefore(fiveMinutesAgo)
    }
}