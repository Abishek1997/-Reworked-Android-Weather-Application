package com.example.weatherApp.data.network.pojos


import com.google.gson.annotations.SerializedName

data class AutocompleteResponse(
    val predictions: List<String>
)