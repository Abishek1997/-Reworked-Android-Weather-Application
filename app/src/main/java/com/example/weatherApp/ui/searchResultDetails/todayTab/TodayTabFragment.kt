package com.example.weatherApp.ui.searchResultDetails.todayTab

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R
import com.example.weatherApp.ui.customcoroutines.ScopedFragment
import com.example.weatherApp.ui.searchResultDetails.SearchResultDetailsViewModel
import com.example.weatherApp.ui.searchResultDetails.SearchResultDetailsViewModelFactory
import kotlinx.android.synthetic.main.activity_search_result_details.*
import kotlinx.android.synthetic.main.future_weather_detail_fragment.*
import kotlinx.android.synthetic.main.weather_search_result_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class TodayTabFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: SearchResultDetailsViewModelFactory by instance()
    private lateinit var viewModel: SearchResultDetailsViewModel
    private lateinit var temperatureSharedPreferences: SharedPreferences

    companion object {
        fun newInstance() =
            TodayTabFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.today_tab_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchResultDetailsViewModel::class.java)
        temperatureSharedPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)
        bindUI()
    }
    private fun bindUI() = launch {
        val currentWeather = viewModel.currentWeather.await()
        currentWeather.observe(viewLifecycleOwner, Observer{
            if (it == null) {
                group_ready.visibility = View.GONE
                group_loading.visibility = View.VISIBLE
                return@Observer
            }
            val activityToolbar = activity?.toolbarTitle
            activityToolbar?.text = it.location.city
            activityToolbar?.setTypeface(null, Typeface.BOLD)

            val windSpeed = it.currently.windSpeed.toString() + " mph"
            value_wind_speed.text = windSpeed

            val precipitation =  String.format("%.4f", it.currently.precipIntensity) + " mmph"
            value_precipitation.text = precipitation

            val pressure = it.currently.pressure.toString() + " mb"
            value_pressure.text = pressure

            val temperature: String = if (temperatureSharedPreferences.getString("temperatureUnit", String()) == "celcius"){
                ((it.currently.temperature - 32) * 0.55).toInt().toString() + getString(R.string.degree_celcius)
            } else{
                it.currently.temperature.roundToInt().toString() + getString(R.string.degree_fahrenheit)
            }
            value_temperature_low.text = temperature

            setIconFromData(it.currently.icon)

            val ozone = it.currently.ozone.toString() + " DU"
            value_ozone.text = ozone

            val visibility = it.currently.visibility.toString() + " km"
            value_visibility.text = visibility

            val cloudCover = (it.currently.cloudCover * 100).roundToInt().toString()  + " " + "%"
            value_cloud_cover.text = cloudCover

            val humidity = (it.currently.humidity * 100).roundToInt().toString() + " " + "%"
            value_humidity.text = humidity

            setSunriseAndSunsetTimes(it.daily!!.data[0].sunriseTime, it.daily.data[0].sunsetTime, it.location.timezone)
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
    private fun setSunriseAndSunsetTimes(sunriseEpoch: Int, sunsetEpoch: Int, localTimeZone: String){

        val timezoneID = ZoneId.of(localTimeZone)

        val sunriseInstant = Instant.ofEpochSecond(sunriseEpoch.toLong())
        val sunriseDatetime = org.threeten.bp.ZonedDateTime.ofInstant(sunriseInstant, timezoneID)
       
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