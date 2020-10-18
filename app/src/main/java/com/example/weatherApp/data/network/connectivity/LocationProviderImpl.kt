package com.example.weatherApp.data.network.connectivity

import com.example.weatherApp.data.db.entities.LocationEntity

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: LocationEntity): Boolean {
        return true
    }

    override suspend fun getNewLocationString(): String {
        return "Los Angeles"
    }
}