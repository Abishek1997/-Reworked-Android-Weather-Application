package com.example.weatherApp.ui.weatherSearchResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Weatherapplication.R
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.network.pojos.DailyData
import com.example.weatherApp.ui.customcoroutines.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.weather_search_result_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WeatherSearchResultFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: WeatherSearchResultViewModelFactory by instance()
    private val currentCity = "Los Angeles, CA, USA"

    private lateinit var viewModel: WeatherSearchResultViewModel
    private var cityName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_search_result_fragment, container, false)
    }

    fun putArguments(args: Bundle){
        cityName = args.getString("searchQuery").toString()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(WeatherSearchResultViewModel::class.java)
        bindUI(cityName)

    }

    private fun bindUI(city: String) = launch {
        val currentWeather = viewModel.currentWeather.await()
        if (cityName == "") {
            currentWeather.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    group_ready.visibility = View.GONE
                    group_loading.visibility = View.VISIBLE
                    return@Observer
                }

                group_loading.visibility = View.GONE
                group_ready.visibility = View.VISIBLE
                getRawData(it)

            })
        }
    }

    private fun getRawData(it: CurrentWeatherEntity){
        val rawTemperature = it.currently.temperature
        val rawSummary = it.currently.summary
        val rawIcon = it.currently.icon
        setUIFirstCard(rawTemperature, rawSummary, rawIcon)

        val rawHumidity = it.currently.humidity
        val rawWindSpeed = it.currently.windSpeed
        val rawVisibility = it.currently.visibility
        val rawPressure = it.currently.pressure
        setUISecondCard(rawHumidity, rawWindSpeed, rawVisibility, rawPressure)

        val rawDailyData = it.daily.data
        setUIThirdCard(rawDailyData.toRecyclerViewItem())
    }

    private fun List<DailyData>.toRecyclerViewItem(): List<RecyclerViewItem>{
        return this.map {
            RecyclerViewItem(it)
        }
    }
    private fun setUIFirstCard(rawTemperature: Double, rawSummary: String, rawIcon: String) {
        textView_city.text = currentCity
        val temperature = rawTemperature.roundToInt().toString() + getString(R.string.degree_fahrenheit)
        textView_temperature.text = temperature
        textView_summary.text = rawSummary
        setIconFromData(rawIcon)
    }

    private fun setUISecondCard(
        rawHumidity: Double,
        rawWindSpeed: Double,
        rawVisibility: Double,
        rawPressure: Double
    ){
        val humidity = rawHumidity.roundToInt().toString() + getString(R.string.percent_symbol)
        val windSpeed = rawWindSpeed.roundToInt().toString() + getString(R.string.space) + getString(
            R.string.windSpeed_unit
        )
        val visibility = rawVisibility.toString() + getString(R.string.space) + getString(R.string.visibility_unit)
        val pressure = rawPressure.toString() + getString(R.string.space) + getString(R.string.pressure_unit)
        textView_humidity_value.text = humidity
        textView_windSpeed_value.text = windSpeed
        textView_visibility_value.text = visibility
        textView_pressure_value.text = pressure
    }
    private fun setIconFromData(rawIcon: String){
        when (rawIcon) {
            "clear-day" -> imageView_icon.setImageResource(R.drawable.weather_sunny)
            "clear-night" -> imageView_icon.setImageResource(R.drawable.weather_night)
            "rain" -> imageView_icon.setImageResource(R.drawable.weather_rainy)
            "sleet" -> imageView_icon.setImageResource(R.drawable.weather_snowy_rainy)
            "snow" -> imageView_icon.setImageResource(R.drawable.weather_snowy)
            "wind" -> imageView_icon.setImageResource(R.drawable.weather_windy_variant)
            "fog" -> imageView_icon.setImageResource(R.drawable.weather_fog)
            "cloudy" -> imageView_icon.setImageResource(R.drawable.weather_cloudy)
            "partly-cloudy-night" -> imageView_icon.setImageResource(R.drawable.weather_night_partly_cloudy)
            "partly-cloudy-day" -> imageView_icon.setImageResource(R.drawable.weather_partly_cloudy)
        }
    }

    private fun setUIThirdCard(items: List<RecyclerViewItem>){
        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply{
            layoutManager = LinearLayoutManager(this@WeatherSearchResultFragment.context)
            adapter = groupieAdapter
        }
    }
}