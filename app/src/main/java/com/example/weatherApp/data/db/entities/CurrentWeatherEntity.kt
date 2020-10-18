package com.example.weatherApp.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherApp.data.network.pojos.*

const val PRIMARY_KEY = 0
@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = PRIMARY_KEY,
    val alerts: List<Alert>?,
    @Embedded(prefix = "currently_")
    val currently: Currently,
    @Embedded(prefix = "daily_")
    val daily: Daily,
    @Embedded(prefix = "hourly_")
    val hourly: Hourly,
    @Embedded(prefix = "minutely_")
    val minutely: Minutely?,
    val offset: Double,
    @Embedded(prefix = "location_")
    val location: LocationEntity
)