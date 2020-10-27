package com.example.weatherApp.data.db.entities

data class LocationEntity(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timeEpoch: Long,
    val city: String
)
