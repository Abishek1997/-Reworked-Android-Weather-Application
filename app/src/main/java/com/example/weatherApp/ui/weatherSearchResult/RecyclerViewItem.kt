package com.example.weatherApp.ui.weatherSearchResult

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.example.Weatherapplication.R
import com.example.weatherApp.data.network.pojos.DailyData
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.recycler_view_list_item.*
import kotlinx.android.synthetic.main.weather_search_result_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewItem(
    private val dailyWeatherEntry: DailyData
) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            val dateString = setDate(dailyWeatherEntry.time)
            recyclerView_card_date.text = dateString

            val rawIcon = dailyWeatherEntry.icon
            recyclerView_card_icon.setImageResource(setIcon(rawIcon))

            recyclerView_card_tempMin.text = dailyWeatherEntry.temperatureLow.toString()
            recyclerView_card_tempMax.text = dailyWeatherEntry.temperatureHigh.toString()
        }
    }

    override fun getLayout() = R.layout.recycler_view_list_item

    @SuppressLint("SimpleDateFormat")
    private fun setDate(rawDate: Int): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(rawDate.toLong() * 1000)
        return dateFormat.format(netDate)

    }

    private fun setIcon(rawIcon: String): Int {
         return when (rawIcon) {
            "clear-day" -> R.drawable.weather_sunny
            "clear-night" -> R.drawable.weather_night
            "rain" -> R.drawable.weather_rainy
            "sleet" -> R.drawable.weather_snowy_rainy
            "snow" -> R.drawable.weather_snowy
            "wind" -> R.drawable.weather_windy_variant
            "fog" -> R.drawable.weather_fog
            "cloudy" -> R.drawable.weather_cloudy
            "partly-cloudy-night" -> R.drawable.weather_night_partly_cloudy
            "partly-cloudy-day" -> R.drawable.weather_partly_cloudy
            else -> R.drawable.weather_cloudy
        }
    }
}