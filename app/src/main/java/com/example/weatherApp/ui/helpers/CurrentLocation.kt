package com.example.weatherApp.ui.helpers

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.network.pojos.LocationObject

interface CurrentLocation {
    val location: LiveData<LocationObject>
    fun getCurrentLocation()
}