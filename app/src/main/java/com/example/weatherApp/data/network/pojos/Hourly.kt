package com.example.weatherApp.data.network.pojos


data class Hourly(
    val data: List<HourlyData>,
    val icon: String,
    val summary: String
)