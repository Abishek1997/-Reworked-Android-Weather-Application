package com.example.weatherApp.data.network.connectivity

import okhttp3.Interceptor

interface ConnectivityInterceptor : Interceptor {
    fun isOnline(): Boolean
}