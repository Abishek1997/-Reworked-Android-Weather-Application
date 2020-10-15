package com.example.weatherApp.data.network.pojos

import com.example.weatherApp.data.network.pojos.*


data class CurrentWeatherResponse(
    val alerts: List<Alert>,
    val currently: Currently,
    val daily: Daily,
    val hourly: Hourly,
    val latitude: Double,
    val longitude: Double,
    val minutely: Minutely,
    val offset: Int,
    val timezone: String
)