package com.example.weatherApp.ui.weatherSearchResult.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.Weatherapplication.R
import kotlinx.android.synthetic.main.activity_weather_search_result.*

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var initEditor: SharedPreferences.Editor
    private lateinit var initPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        sharedPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)
        addPreferencesFromResource(R.xml.temperature_preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = "Settings"
        activity?.toolbar_title?.text = title
        activity?.toolbar_title?.setTypeface(null, Typeface.BOLD)
        initPreferences = requireContext().getSharedPreferences("Favorites", Context.MODE_PRIVATE)

        val switchTheme = findPreference<SwitchPreference>("SWITCH_THEME")
        switchTheme!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue -> // He
                editor = sharedPreferences.edit()
                if (newValue == false){
                    editor.putString("theme", "light")
                    } else{
                    editor.putString("theme", "dark")
                }
                editor.apply()
                requireActivity().recreate()
                true
            }

        val switchTemperatureUnit = findPreference<SwitchPreference>("USE_CELCIUS")
        switchTemperatureUnit!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{ _, newValue ->
                editor = sharedPreferences.edit()
                if (newValue == true){
                    editor.putString("temperatureUnit", "celcius")
                } else{
                    editor.putString("temperatureUnit", "fahrenheit")
                }
                editor.apply()
                requireActivity().recreate()
                true
            }

        val switchLocationPreferences = findPreference<SwitchPreference>("SWITCH_CURRENT_LOCATION_PREFERENCES")
        switchLocationPreferences!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                editor = sharedPreferences.edit()
                if (newValue == false){
                    editor.putString("currentLocationPreference", "no")
                } else{
                    editor.putString("currentLocationPreference", "yes")
                    initEditor = initPreferences.edit()
                    initEditor.remove("initData")
                    initEditor.apply()
                }
                editor.apply()
                requireActivity().recreate()
                true
            }
    }
}