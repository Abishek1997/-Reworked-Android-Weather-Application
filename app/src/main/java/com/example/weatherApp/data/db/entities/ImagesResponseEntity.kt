package com.example.weatherApp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PRIMARY_KEY_CITY = 0

@Entity(tableName = "city_images")
data class ImagesResponseEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = PRIMARY_KEY,
    val images: List<String>
)