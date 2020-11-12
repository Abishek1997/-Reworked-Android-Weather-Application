package com.example.weatherApp.ui.searchResultDetails.weeklyTab

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.weather_search_result_fragment.*
import kotlinx.android.synthetic.main.weekly_tab_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

class WeeklyTabFragment : ScopedFragment(), KodeinAware{
    override val kodein by closestKodein()
    private val viewModelFactory: SearchResultDetailsViewModelFactory by instance()

    private lateinit var temperatureSharedPreferences: SharedPreferences
    private lateinit var viewModel: SearchResultDetailsViewModel

    companion object {
        fun newInstance() =
            WeeklyTabFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weekly_tab_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchResultDetailsViewModel::class.java)
        temperatureSharedPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)
        bindUI()
    }
    private fun bindUI() = launch {
        val temperatureLow = ArrayList<Entry>()
        val temperatureHigh = ArrayList<Entry>()

        val currentWeather = viewModel.currentWeather.await()
        currentWeather.observe(viewLifecycleOwner, Observer { data ->
            if (data == null) {
                group_ready.visibility = View.GONE
                group_loading.visibility = View.VISIBLE
                return@Observer
            }

            if (temperatureSharedPreferences.getString("temperatureUnit", String()) == "celcius"){
                data.daily!!.data.forEachIndexed { index, dailyData ->
                    temperatureLow.add(Entry(index.toFloat(), ((dailyData.temperatureLow - 32) * (0.55)).toFloat()))
                    temperatureHigh.add(Entry(index.toFloat(), ((dailyData.temperatureHigh - 32) * (0.55)).toFloat()))
                }

            } else{
                data.daily!!.data.forEachIndexed { index, dailyData ->
                    temperatureLow.add(Entry(index.toFloat(), dailyData.temperatureLow.toFloat()))
                    temperatureHigh.add(Entry(index.toFloat(), dailyData.temperatureHigh.toFloat()))
                }
            }

            weekly_summary_text.text = data.daily.summary
            setChartUI(temperatureLow, temperatureHigh)
            setIconFromData(data.daily.icon)
        })
    }

    private fun setChartUI(temperatureLow: ArrayList<Entry>, temperatureHigh: ArrayList<Entry>){
        val temperatureLowString: String = if (temperatureSharedPreferences.getString("temperatureUnit", String()) == "celcius"){
            "Minimum Temperature" + getString(R.string.degree_celcius)
        } else{
            "Minimum Temperature" + getString(R.string.degree_fahrenheit)
        }

        val temperatureHighString: String = if (temperatureSharedPreferences.getString("temperatureUnit", String()) == "celcius"){
            "Maximum Temperature" + getString(R.string.degree_celcius)
        } else{
            "Maximum Temperature" + getString(R.string.degree_fahrenheit)
        }
        val temperatureLowLData = LineDataSet(temperatureLow, temperatureLowString)
        val temperatureHighData = LineDataSet(temperatureHigh, temperatureHighString)

        val lineDataSets = ArrayList<ILineDataSet>()

        temperatureHighData.color = Color.rgb(106, 89, 53)
        temperatureHighData.setCircleColor(Color.WHITE)
        temperatureHighData.lineWidth = 2f
        temperatureLowLData.color = Color.rgb(80, 74, 100)
        temperatureLowLData.setCircleColor(Color.WHITE)
        temperatureLowLData.lineWidth = 2f

        lineDataSets.add(temperatureLowLData)
        lineDataSets.add(temperatureHighData)

        lineChart.axisLeft.textColor = Color.rgb(126, 126, 126)
        lineChart.axisRight.textColor = Color.rgb(126, 126, 126)
        lineChart.xAxis.textColor = Color.rgb(126, 126, 126)
        lineChart.legend.textColor = Color.WHITE
        lineChart.legend.textSize = 15f

        lineChart.xAxis.setDrawGridLines(false)

        lineChart.data = LineData(lineDataSets)

        lineChart.invalidate()
    }

    private fun setIconFromData(rawIcon: String){
        val imageViewIcon = weekly_summary_icon
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
}