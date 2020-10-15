package com.example.weatherApp.data.network.pojos


data class Minutely(
    val data: List<MinutelyData>,
    val icon: String,
    val summary: String
)