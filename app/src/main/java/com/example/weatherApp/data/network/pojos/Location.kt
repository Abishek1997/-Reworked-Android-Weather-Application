package com.example.weatherApp.data.network.pojos

data class Location(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timeEpoch: Long,
    val city: String
)
