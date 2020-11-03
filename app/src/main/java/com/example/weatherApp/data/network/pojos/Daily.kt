package com.example.weatherApp.data.network.pojos

import com.example.weatherApp.data.network.pojos.DailyData

data class Daily(
    val summary: String,
    val icon: String,
    val data: List<DailyData>
)