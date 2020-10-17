package com.example.weatherApp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.weatherApp.data.network.pojos.Alert
import com.google.gson.Gson

class AlertConverter {
    @TypeConverter
    fun alertListToJson(value: List<Alert>?): String?{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun alertJsonToList(value: String?): List<Alert>? {
        return Gson().fromJson(value, Array<Alert>::class.java)?.toList()
    }
}