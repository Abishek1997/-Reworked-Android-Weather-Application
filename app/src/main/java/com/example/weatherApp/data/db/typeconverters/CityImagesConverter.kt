package com.example.weatherApp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.weatherApp.data.network.pojos.DailyData
import com.google.gson.Gson

class CityImagesConverter {

    @TypeConverter
    fun cityListToJson(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToCityList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}