package com.example.weatherApp.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherApp.data.db.entities.ImagesResponseEntity
import com.example.weatherApp.data.db.entities.PRIMARY_KEY_CITY

@Dao
interface CityImagesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCityImages(cityImagesResponse: ImagesResponseEntity)

    @Query(value = "Select * from city_images where id = $PRIMARY_KEY_CITY")
    fun getCityImagesFromDB(): LiveData<ImagesResponseEntity>

    @Query(value = "Select * from city_images where id = $PRIMARY_KEY_CITY")
    fun getCityFromDB(): ImagesResponseEntity?
}