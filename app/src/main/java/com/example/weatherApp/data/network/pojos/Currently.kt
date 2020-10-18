package com.example.weatherApp.data.network.pojos

data class Currently(
    val apparentTemperature: Double,
    val cloudCover: Double,
    val dewPoint: Double,
    val humidity: Double,
    val icon: String,
    val ozone: Double,
    val precipIntensity: Double,
    val precipProbability: Double,
    val pressure: Double,
    val summary: String,
    val temperature: Double,
    val time: Long,
    val uvIndex: Double,
    val visibility: Double,
    val windSpeed: Double
)