package com.example.weatherApp.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.network.pojos.Currently
import com.example.weatherApp.data.db.entities.PRIMARY_KEY
import com.example.weatherApp.data.network.pojos.CurrentWeatherResponse

@Dao
interface CurrentWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCurrentWeatherData(currentWeatherEntry: CurrentWeatherEntity)

    @Query (value = "Select * from current_weather where id = $PRIMARY_KEY")
    fun getCurrentWeatherFromDB(): LiveData<CurrentWeatherEntity>

    @Query (value = "Select * from current_weather where id = $PRIMARY_KEY")
    fun getDataFromDB(): CurrentWeatherEntity
}