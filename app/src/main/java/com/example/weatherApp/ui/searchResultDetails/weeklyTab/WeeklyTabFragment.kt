package com.example.weatherApp.ui.searchResultDetails.weeklyTab

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

class WeeklyTabFragment : ScopedFragment(), KodeinAware{
    override val kodein by closestKodein()
    private val viewModelFactory: SearchResultDetailsViewModelFactory by instance()
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
            data.daily!!.data.forEachIndexed { index, dailyData ->
                temperatureLow.add(Entry(index.toFloat(), dailyData.temperatureLow.toFloat()))
                temperatureHigh.add(Entry(index.toFloat(), dailyData.temperatureHigh.toFloat()))
            }

            weekly_summary_text.text = data.daily.summary
            setChartUI(temperatureLow, temperatureHigh)
            setIconFromData(data.daily.icon)
        })
    }

    private fun setChartUI(temperatureLow: ArrayList<Entry>, temperatureHigh: ArrayList<Entry>){
        val temperatureLowLData = LineDataSet(temperatureLow, "Minimum Temperature")
        val temperatureHighData = LineDataSet(temperatureHigh, "Minimum Temperature")

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