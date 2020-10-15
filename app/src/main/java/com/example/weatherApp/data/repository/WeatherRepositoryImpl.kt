package com.example.weatherApp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.daos.CurrentWeatherDAO
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.network.connectivity.WeatherNetworkDataSource
import com.example.weatherApp.data.network.pojos.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class WeatherRepositoryImpl(
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : WeatherRepository {

    init{
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever{ newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }
    override suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDAO.getCurrentWeatherFromDB()
        }
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