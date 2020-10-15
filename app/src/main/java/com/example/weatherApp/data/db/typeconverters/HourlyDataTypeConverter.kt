package com.example.weatherApp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.weatherApp.data.network.pojos.HourlyData
import com.google.gson.Gson

class HourlyDataTypeConverter {

    @TypeConverter
    fun hourlyListToJson(value: List<HourlyData>?): String = Gson().toJson(value)

    @TypeConverter
    fun hourlyJsonToList(value: String) = Gson().fromJson(value, Array<HourlyData>::class.java).toList()

}