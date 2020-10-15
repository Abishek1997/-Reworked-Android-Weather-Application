package com.example.weatherApp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.weatherApp.data.network.pojos.Alert
import com.example.weatherApp.data.network.pojos.DailyData
import com.google.gson.Gson

class AlertConverter {
    @TypeConverter
    fun alertListToJson(value: List<Alert>?): String = Gson().toJson(value)

    @TypeConverter
    fun alertJsonToList(value: String) = Gson().fromJson(value, Array<Alert>::class.java).toList()
}