package com.example.weatherApp.ui.weatherSearchResult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R
import com.example.weatherApp.data.network.connectivity.ConnectivityInterceptorImpl
import com.example.weatherApp.data.network.DarkskyWeatherApiService
import com.example.weatherApp.data.network.connectivity.WeatherNetworkDataSourceImpl
import kotlinx.android.synthetic.main.weather_search_result_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherSearchResultFragment : Fragment() {

    companion object {
        fun newInstance() =
            WeatherSearchResultFragment()
    }

    private lateinit var viewModel: WeatherSearchResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_search_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherSearchResultViewModel::class.java)
        val apiService = DarkskyWeatherApiService(ConnectivityInterceptorImpl(this.requireContext()))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
        val textView = textView

        weatherNetworkDataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, {
            textView.text = it.alerts.toString()
        })
        GlobalScope.launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchCurrentWeather(34.04563903808594, -118.24163818359375)
        }
    }
}