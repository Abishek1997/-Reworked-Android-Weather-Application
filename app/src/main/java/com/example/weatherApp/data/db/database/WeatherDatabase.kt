package com.example.weatherApp.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherApp.data.db.daos.CurrentWeatherDAO
import com.example.weatherApp.data.db.daos.WeatherLocationDAO
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.db.entities.LocationEntity
import com.example.weatherApp.data.db.typeconverters.AlertConverter
import com.example.weatherApp.data.db.typeconverters.DailyDataTypeConverter
import com.example.weatherApp.data.db.typeconverters.HourlyDataTypeConverter
import com.example.weatherApp.data.db.typeconverters.MinutelyDataTypeConverter

@Database(
    entities = [CurrentWeatherEntity::class, LocationEntity::class],
    version = 3
)
@TypeConverters(DailyDataTypeConverter::class, AlertConverter::class, HourlyDataTypeConverter::class, MinutelyDataTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getCurrentWeatherDao(): CurrentWeatherDAO
    abstract fun getWeatherLocationDao(): WeatherLocationDAO

    companion object{
        @Volatile private var instance: WeatherDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java, "WeatherDatabase.db")
                .build()
    }

}