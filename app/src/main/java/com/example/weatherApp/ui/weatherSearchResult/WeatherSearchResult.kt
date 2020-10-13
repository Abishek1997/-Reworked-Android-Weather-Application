package com.example.weatherApp.ui.weatherSearchResult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R

class WeatherSearchResult : Fragment() {

    companion object {
        fun newInstance() =
            WeatherSearchResult()
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
        // TODO: Use the ViewModel
    }

}