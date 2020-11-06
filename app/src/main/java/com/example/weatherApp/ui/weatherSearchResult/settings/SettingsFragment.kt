package com.example.weatherApp.ui.weatherSearchResult.settings

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.Weatherapplication.R
import kotlinx.android.synthetic.main.activity_weather_search_result.*

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.temperature_preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = "Settings"
        activity?.toolbar_title?.text = title
        activity?.toolbar_title?.setTypeface(null, Typeface.BOLD)
    }
}