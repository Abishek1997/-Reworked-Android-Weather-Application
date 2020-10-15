package com.example.weatherApp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.weatherApp.data.network.pojos.DailyData
import com.google.gson.Gson

class DailyDataTypeConverter {

    @TypeConverter
    fun listToJson(value: List<DailyData>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<DailyData>::class.java).toList()
}