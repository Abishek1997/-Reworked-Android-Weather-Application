package com.example.weatherApp.data.network

import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.network.connectivity.ConnectivityInterceptor
import com.example.weatherApp.data.network.pojos.AutocompleteResponse
import com.example.weatherApp.data.network.pojos.CurrentWeatherResponse
import com.example.weatherApp.data.network.pojos.ImagesResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "ab5f43553f9630945af0a4a8c0c3e4f2"

// http://10.0.2.2:8081/currentLocationWeather?lat=34.04563903808594&lon=-118.24163818359375
interface DarkskyWeatherApiService {

    @GET("currentLocationWeather")
    fun getCurrentWeatherAsync(
        @Query(value = "lat") latitude: Double,
        @Query(value = "lon") longitude: Double
    ): Deferred<CurrentWeatherEntity>

    @GET("searchLocationWeather")
    fun getSearchedWeatherAsync(
        @Query(value = "city") city: String
    ): Deferred<CurrentWeatherEntity>

    @GET("cityImages")
    fun getCityImagesAsync(
        @Query(value = "city") city: String
    ): Deferred<ImagesResponseEntity>

    @GET("autoComplete")
    fun getAutocompleteCitiesAsync(
        @Query(value = "city") city: String
    ): Deferred<AutocompleteResponse>

    companion object{
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): DarkskyWeatherApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://10.0.2.2:8081/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DarkskyWeatherApiService::class.java)
        }
    }
}