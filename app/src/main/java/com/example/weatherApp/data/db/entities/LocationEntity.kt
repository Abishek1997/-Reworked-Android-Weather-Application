package com.example.weatherApp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val WEATHER_LOCATION_ID = 0

@Entity(tableName = "weather_location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = WEATHER_LOCATION_ID,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timeEpoch: Long,
    val city: String
)
