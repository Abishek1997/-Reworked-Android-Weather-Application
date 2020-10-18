package com.example.weatherApp.data.network.connectivity

import com.example.weatherApp.data.db.entities.LocationEntity

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: LocationEntity): Boolean
    suspend fun getNewLocationString(): String
}