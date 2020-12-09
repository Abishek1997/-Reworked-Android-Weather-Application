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

class CurrentLocationAccessImpl(
    private val context: Context
) : CurrentLocationAccess {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        context
    )
    private val _locationObject = MutableLiveData<LocationObject>()
    private val geoCoder: Geocoder = Geocoder(context, Locale.getDefault())
    override val locationObject: LiveData<LocationObject>
        get() = _locationObject

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
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val addresses: List<Address> = geoCoder.getFromLocation(it.latitude, it.longitude, 1)
            val tempLocationObject = LocationObject(0.0, 0.0, "")
            tempLocationObject.latitude = it.latitude
            tempLocationObject.longitude = it.longitude
            tempLocationObject.city = "${addresses[0].locality}, ${addresses[0].adminArea}, ${addresses[0].countryName}"
            _locationObject.value = tempLocationObject
        }
    }
}