package com.example.weatherApp.data.network.pojos


data class DailyData(
    val cloudCover: Double,
    val dewPoint: Double,
    val humidity: Double,
    val icon: String,
    val ozone: Double,
    val precipIntensity: Double,
    val precipIntensityMax: Double,
    val precipProbability: Double,
    val precipType: String,
    val pressure: Double,
    val summary: String,
    val sunriseTime: Int,
    val sunsetTime: Int,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val time: Long,
    val uvIndex: Double,
    val visibility: Double,
    val windSpeed: Double,
    val index: Int
)