package com.example.weatherApp.ui.helpers

import androidx.lifecycle.LiveData
import com.example.weatherApp.data.network.pojos.LocationObject

interface CurrentLocationAccess {
    val locationObject: LiveData<LocationObject>

    fun getCurrentLocation()
}