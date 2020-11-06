package com.example.weatherApp.ui.searchResultDetails

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.Weatherapplication.R
import kotlinx.android.synthetic.main.activity_search_result_details.*

class SearchResultDetailsActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var themeSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        themeSharedPreferences = applicationContext.getSharedPreferences("Theme", Context.MODE_PRIVATE)
        if (themeSharedPreferences.getString("theme", String()) == "light"){
            setTheme(R.style.ActivityLightTheme)
        } else{
            setTheme(R.style.ActivityThemeDark)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result_details)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNav.setupWithNavController(navController)
    }
}