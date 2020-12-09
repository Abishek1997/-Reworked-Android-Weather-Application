package com.example.weatherApp.ui.weatherSearchResult.futureWeather

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R
import com.example.weatherApp.ui.customcoroutines.ScopedFragment
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModel
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModelFactory
import kotlinx.android.synthetic.main.activity_weather_search_result.*
import kotlinx.android.synthetic.main.future_weather_detail_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class FutureWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: WeatherSearchResultViewModelFactory by instance()
    private lateinit var viewModel: WeatherSearchResultViewModel
    private lateinit var temperatureSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val index = arguments?.let { FutureWeatherFragmentArgs.fromBundle(it) }
        temperatureSharedPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(WeatherSearchResultViewModel::class.java)
        bindUI(index!!.dayIndex)
    }
    private fun bindUI(index: Int) = launch {
        val bottomNav = activity?.bottomNav_home
        bottomNav?.visibility = View.GONE

        val futureWeather = viewModel.currentWeather()
        futureWeather.observe(viewLifecycleOwner, Observer {
            if (it.daily?.data == null){
                return@Observer
            }

            val windSpeed = it.daily.data[index].windSpeed.toString() + " mph"
            value_wind_speed.text = windSpeed

            val precipitation =  String.format("%.4f", it.daily.data[index].precipIntensity) + " mmph"
            value_precipitation.text = precipitation

            val pressure = it.daily.data[index].pressure.toString() + " mb"
            value_pressure.text = pressure

            val temperatureLow: String = if (temperatureSharedPreferences.getString("temperatureUnit", String()) == "celcius"){
                ((it.daily.data[index].temperatureLow - 32) * 0.55).toInt().toString() + " \u2103"
            } else{
                it.daily.data[index].temperatureLow.roundToInt().toString() + " \u2109"
            }



            value_temperature_low.text = temperatureLow

            Summary.text = it.daily.data[index].summary

            val temperatureHigh: String = if (temperatureSharedPreferences.getString("temperatureUnit", String()) == "celcius"){
                ((it.daily.data[index].temperatureHigh - 32) * 0.55).toInt().toString() + " \u2103"
            } else{
                it.daily.data[index].temperatureHigh.roundToInt().toString() + " \u2109"
            }
            value_ozone.text = temperatureHigh

            val visibility = it.daily.data[index].visibility.toString() + " km"
            value_visibility.text = visibility

            val cloudCover = (it.daily.data[index].cloudCover * 100).roundToInt().toString()  + " " + "%"
            value_cloud_cover.text = cloudCover

            val humidity = (it.daily.data[index].humidity * 100).roundToInt().toString() + " " + "%"
            value_humidity.text = humidity

            setIconFromData(it.daily.data[index].icon)

            setSunriseAndSunsetTimes(it.daily.data[index].sunriseTime, it.daily.data[index].sunsetTime, it.location.timezone)
        })
    }

    private fun setIconFromData(rawIcon: String){
        val imageViewIcon = image_summary
        when (rawIcon) {
            "clear-day" -> imageViewIcon.setImageResource(R.drawable.weather_sunny)
            "clear-night" -> imageViewIcon.setImageResource(R.drawable.weather_night)
            "rain" -> imageViewIcon.setImageResource(R.drawable.weather_rainy)
            "sleet" -> imageViewIcon.setImageResource(R.drawable.weather_snowy_rainy)
            "snow" -> imageViewIcon.setImageResource(R.drawable.weather_snowy)
            "wind" -> imageViewIcon.setImageResource(R.drawable.weather_windy_variant)
            "fog" -> imageViewIcon.setImageResource(R.drawable.weather_fog)
            "cloudy" -> imageViewIcon.setImageResource(R.drawable.weather_cloudy)
            "partly-cloudy-night" -> imageViewIcon.setImageResource(R.drawable.weather_night_partly_cloudy)
            "partly-cloudy-day" -> imageViewIcon.setImageResource(R.drawable.weather_partly_cloudy)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setSunriseAndSunsetTimes(sunriseEpoch: Int, sunsetEpoch: Int, localTimeZone: String){

        val timezoneID = ZoneId.of(localTimeZone)

        val sunriseInstant = Instant.ofEpochSecond(sunriseEpoch.toLong())
        val sunriseDatetime = org.threeten.bp.ZonedDateTime.ofInstant(sunriseInstant, timezoneID)
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(sunriseEpoch.toLong() * 1000)
        date.text = dateFormat.format(netDate)
        val sunriseHour = sunriseDatetime.hour
        val sunriseMinute = sunriseDatetime.minute
        val sunriseTime = String.format("%02d:%02d AM", sunriseHour, sunriseMinute)
        value_sunrise.text = sunriseTime

        val sunsetInstant = Instant.ofEpochSecond(sunsetEpoch.toLong())
        val sunsetDatetime = org.threeten.bp.ZonedDateTime.ofInstant(sunsetInstant, timezoneID)
        var sunsetHour = sunriseDatetime.hour
        if (sunsetHour > 12)
            sunsetHour %= 12
        val sunsetMinute = sunsetDatetime.minute
        val sunsetTime = String.format("%02d:%02d PM", sunsetHour, sunsetMinute)
        value_sunset.text = sunsetTime
    }
}