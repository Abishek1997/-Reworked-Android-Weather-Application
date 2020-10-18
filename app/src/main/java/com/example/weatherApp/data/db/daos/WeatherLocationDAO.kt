package com.example.weatherApp.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.LocationEntity
import com.example.weatherApp.data.db.entities.PRIMARY_KEY
import com.example.weatherApp.data.db.entities.WEATHER_LOCATION_ID

@Dao
interface WeatherLocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertWeatherLocation(weatherLocation: LocationEntity)

    @Query(value = "Select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getWeatherLocationFromDB(): LiveData<LocationEntity>
}