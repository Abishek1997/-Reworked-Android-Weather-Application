package com.example.weatherApp.ui.weatherSearchResult

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.Weatherapplication.R
import kotlinx.android.synthetic.main.activity_search_result_details.*

class WeatherSearchResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_search_result)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES)
        setSupportActionBar(toolbar)
    }
}