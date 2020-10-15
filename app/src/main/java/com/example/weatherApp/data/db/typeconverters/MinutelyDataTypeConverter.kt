package com.example.weatherApp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.weatherApp.data.network.pojos.HourlyData
import com.example.weatherApp.data.network.pojos.MinutelyData
import com.google.gson.Gson

class MinutelyDataTypeConverter {
    @TypeConverter
    fun hourlyListToJson(value: List<MinutelyData>?): String = Gson().toJson(value)

    @TypeConverter
    fun hourlyJsonToList(value: String) = Gson().fromJson(value, Array<MinutelyData>::class.java).toList()

}