package com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Weatherapplication.R
import com.example.weatherApp.data.db.entities.CurrentWeatherEntity
import com.example.weatherApp.data.network.pojos.DailyData
import com.example.weatherApp.ui.customcoroutines.ScopedFragment
import com.example.weatherApp.ui.helpers.RecyclerViewItem
import com.example.weatherApp.ui.searchResultDetails.SearchResultDetailsActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_weather_search_result.*
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
    private lateinit var viewModel: WeatherSearchResultViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var temperatureUnitPreferences: SharedPreferences
    private lateinit var currentLocationPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireContext().getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        temperatureUnitPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)
        currentLocationPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)

        return inflater.inflate(R.layout.weather_search_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(WeatherSearchResultViewModel::class.java)
        bindUI()
        card_view_1.setOnClickListener {
            val intent = Intent(activity, SearchResultDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindUI() = launch {

        val toolBar = activity?.toolbar
        toolBar?.visibility = View.VISIBLE
        lateinit var currentWeather: LiveData<out CurrentWeatherEntity>

        val bottomNav = activity?.bottomNav_home
        bottomNav?.visibility = View.VISIBLE
        if (!sharedPreferences.contains("initData")){
            if((currentLocationPreferences.getString("currentLocationPreference", String()) == "no")){
                Log.d("data", "no")
            } else{
                currentWeather = viewModel.currentWeather.await()
            }
            editor = sharedPreferences.edit()
            editor.putString("initData", "available")
            editor.apply()
        }
        else{
            currentWeather = viewModel.getWeatherData()
        }
        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                group_ready.visibility = View.GONE
                group_loading.visibility = View.VISIBLE
                return@Observer
            }

            if (it.location.latitude == 34.04563903808594 && it.location.longitude == -118.24163818359375)
                floatingActionButton.visibility = View.GONE
            else
                floatingActionButton.visibility = View.VISIBLE

            group_loading.visibility = View.GONE
            group_ready.visibility = View.VISIBLE
            getRawData(it)
            setSharedPreferences(it)
        })
    }

    private fun getRawData(it: CurrentWeatherEntity){

        val activityToolbarTitle = activity?.toolbar_title
        val city = it.location.city
        textView_city.text = city
        activityToolbarTitle?.text = city
        activityToolbarTitle?.setTypeface(null, Typeface.BOLD)
        val rawTemperature = it.currently.temperature
        val rawSummary = it.currently.summary
        val rawIcon = it.currently.icon
        setUIFirstCard(rawTemperature, rawSummary, rawIcon)

        val rawHumidity = it.currently.humidity
        val rawWindSpeed = it.currently.windSpeed
        val rawVisibility = it.currently.visibility
        val rawPressure = it.currently.pressure
        setUISecondCard(rawHumidity, rawWindSpeed, rawVisibility, rawPressure)

        val rawDailyData = it.daily?.data
        setUIThirdCard(rawDailyData?.toRecyclerViewItem())
    }

    private fun List<DailyData>.toRecyclerViewItem(): List<RecyclerViewItem>{
        val temperaturePreferences = temperatureUnitPreferences.getString("temperatureUnit", String())
        return this.map {
            RecyclerViewItem(it, temperaturePreferences)
        }
    }
    private fun setUIFirstCard(rawTemperature: Double, rawSummary: String, rawIcon: String) {

        val temperature: String = if (temperatureUnitPreferences.getString("temperatureUnit", String()) == "celcius"){
            ((rawTemperature - 32) * 0.55).toInt().toString() + getString(R.string.degree_celcius)
        } else{
            rawTemperature.roundToInt().toString() + getString(R.string.degree_fahrenheit)
        }
        textView_temperature.text = temperature
        val summary: String = if (rawSummary.length > 20){
            (rawSummary.subSequence(0, 20) as String) + "-\n" + (rawSummary.subSequence(20, rawSummary.length))
        } else{
            rawSummary
        }
        textView_summary.text = summary
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

    private fun setUIThirdCard(items: List<RecyclerViewItem>?){
        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply {
            if (items != null) {
                addAll(items)
            }
        }
        recyclerView.apply{
            layoutManager = LinearLayoutManager(this@WeatherSearchResultFragment.context)
            adapter = groupieAdapter
        }
        groupieAdapter.setOnItemClickListener{ item, view ->
            (item as? RecyclerViewItem).let {
                val actionToFutureFragment = WeatherSearchResultFragmentDirections.actionFuture(it!!.dailyWeatherEntry.index)
                Navigation.findNavController(view).navigate(actionToFutureFragment)
            }
        }
    }

    private fun setSharedPreferences(weatherEntity: CurrentWeatherEntity){
        if (sharedPreferences.contains(weatherEntity.location.city)){
            floatingActionButton.setImageResource(R.drawable.map_minus_icon)
        }else{
            floatingActionButton.setImageResource(R.drawable.map_plus_icon)
        }

        floatingActionButton.setOnClickListener {
            editor = sharedPreferences.edit()
            if (! sharedPreferences.contains(weatherEntity.location.city)){
                val toast = Toast.makeText(
                    context,
                    "${weatherEntity.location.city} was added to favorites", Toast.LENGTH_SHORT
                )
                toast.show()
                editor.putString(weatherEntity.location.city, weatherEntity.location.city)
                floatingActionButton.setImageResource(R.drawable.map_minus_icon)
            }else{
                val toast: Toast = Toast.makeText(
                    context,
                    weatherEntity.location.city + " was removed from favorites",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                editor.remove(weatherEntity.location.city)
                floatingActionButton.setImageResource(R.drawable.map_plus_icon)
            }
            editor.apply()
        }
    }

}