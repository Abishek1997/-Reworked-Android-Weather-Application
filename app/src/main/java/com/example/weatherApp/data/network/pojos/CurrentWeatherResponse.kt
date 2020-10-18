package com.example.weatherApp.data.network.pojos


data class CurrentWeatherResponse(
    val alerts: List<Alert>,
    val currently: Currently,
    val daily: Daily,
    val hourly: Hourly,
    val minutely: Minutely,
    val offset: Double,
    val location: Location
)