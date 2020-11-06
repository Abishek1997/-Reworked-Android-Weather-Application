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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        sharedPreferences = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE)
        addPreferencesFromResource(R.xml.temperature_preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = "Settings"
        activity?.toolbar_title?.text = title
        activity?.toolbar_title?.setTypeface(null, Typeface.BOLD)

        val prefs = findPreference<SwitchPreference>("SWITCH_THEME")
        prefs!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue -> // He
                editor = sharedPreferences.edit()
                if (newValue == false){
                    editor.putString("theme", "light")
                    editor.apply()
                    requireActivity().recreate()
                } else{
                    editor.putString("theme", "dark")
                    editor.apply()
                    requireActivity().recreate()
                }
                true
            }

    }
}