package com.example.weatherApp.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherApp.data.db.daos.CityImagesDAO
import com.example.weatherApp.data.db.daos.CurrentWeatherDAO
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.db.entities.LocationEntity
import com.example.weatherApp.data.network.connectivity.WeatherNetworkDataSource
import com.example.weatherApp.data.network.pojos.AutocompleteResponse
import com.example.weatherApp.ui.helpers.CurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class WeatherRepositoryImpl(
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val cityImagesDAO: CityImagesDAO,
    private val currentLocationGetter: CurrentLocation
) : WeatherRepository {

    init{
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever{ newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }

        weatherNetworkDataSource.downloadedCityImages.observeForever{ newCityImages ->
            persistCityImages(newCityImages)
        }

        currentLocationGetter.getCurrentLocation()
        currentLocationGetter.location.observeForever{
            Log.d("data", "location here: $it")
        }
    }

    override suspend fun getWeatherData(): LiveData<out CurrentWeatherEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDAO.getCurrentWeatherFromDB()
        }
    }

    override suspend fun getCityImages(): LiveData<out ImagesResponseEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext cityImagesDAO.getCityImagesFromDB()
        }
    }

    override suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntity> {
        return withContext(Dispatchers.IO) {
            initFetchWeatherData()
            initFetchCityImages()
            return@withContext currentWeatherDAO.getCurrentWeatherFromDB()
        }
    }

    override suspend fun getSearchedWeather(city: String) {
        return withContext(Dispatchers.IO) {
            initFetchSearchedWeatherData(city)
        }
    }

    override suspend fun getSearchedCityImages(city: String) {
        return withContext(Dispatchers.IO) {
            initFetchSearchedCityImages(city)
        }
    }

    override suspend fun getAutocompleteCities(city: String): LiveData<out AutocompleteResponse> {
        fetchAutocompleteCities(city)
        return weatherNetworkDataSource.downloadedAutocompleteCities
    }

    private suspend fun fetchAutocompleteCities(city: String){
        weatherNetworkDataSource.getAutocompleteCities(city)
    }
    private suspend fun initFetchSearchedCityImages(city: String){
        fetchSearchedCityImages(city)
    }

    private suspend fun initFetchSearchedWeatherData(city: String){
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

        if (currentWeatherDAO.getDataFromDB() == null){
            fetchCurrentWeather()
        }
        else{
            val lastLocation: LocationEntity = currentWeatherDAO.getDataFromDB()!!.location

            val lastFetchTimeEpoch: Long = currentWeatherDAO.getDataFromDB()!!.currently.time
            val timeInstant = Instant.ofEpochSecond(lastFetchTimeEpoch)
            val timezone = currentWeatherDAO.getDataFromDB()!!.location.timezone
            val zoneID = ZoneId.of(timezone)
            val zonedDateTime = ZonedDateTime.ofInstant(timeInstant, zoneID)

            if (!lastLocation.latitude.equals(34.04563903808594) || !lastLocation.longitude.equals(-118.24163818359375) || isFetchNeeded(zonedDateTime)) {
                fetchCurrentWeather()
            }
        }
    }

    private suspend fun initFetchCityImages(){
        fetchCityImages()
    }

    private suspend fun fetchSearchedCityImages(city: String){
        weatherNetworkDataSource.getCityImages(city)
    }

    private suspend fun fetchCityImages(){
        weatherNetworkDataSource.getCityImages("Los Angeles")
    }

    private fun persistCityImages(fetchedCityImages: ImagesResponseEntity){
        GlobalScope.launch(Dispatchers.IO) {
            cityImagesDAO.upsertCityImages(fetchedCityImages)
        }
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            34.04563903808594,
            -118.24163818359375
        )
    }

    private fun isFetchNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val result = Duration.between(lastFetchTime, ZonedDateTime.now())
        val threshold = Duration.ofMinutes(15)
        return result > threshold
    }
}