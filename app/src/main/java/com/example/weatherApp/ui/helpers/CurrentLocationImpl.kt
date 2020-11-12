package com.example.weatherApp.ui.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherApp.data.network.pojos.LocationObject
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class CurrentLocationImpl(
    private val context: Context
) : CurrentLocation {
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        context
    )
    private val _location = MutableLiveData<LocationObject>()
    override val location: LiveData<LocationObject>
        get() = _location
    override fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("data", "no permission")
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val city = "${addresses[0].locality}, ${addresses[0].adminArea}, ${addresses[0].countryName}"
                val tempLocation = LocationObject(0.0, 0.0, "")
                tempLocation.city = city
                tempLocation.latitude = it.latitude
                tempLocation.longitude = it.longitude
                Log.d("data", "location: $tempLocation")
                _location.value = tempLocation
            }

    }
}