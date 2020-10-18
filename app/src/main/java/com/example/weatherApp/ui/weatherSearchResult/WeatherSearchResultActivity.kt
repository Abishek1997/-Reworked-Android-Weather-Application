package com.example.weatherApp.ui.weatherSearchResult

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModel
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModelFactory
import kotlinx.android.synthetic.main.activity_search_result_details.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WeatherSearchResultActivity : AppCompatActivity(), KodeinAware{

    override val kodein by closestKodein()
    private val viewModelFactory: WeatherSearchActivityViewmodelFactory by instance()
    private lateinit var viewModel: WeatherSearchActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_search_result)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(WeatherSearchActivityViewModel::class.java)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu_item, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                runBlocking {
                    viewModel.setQueryToRepository(query)
                }
                searchView.setQuery("", false)
                searchView.clearFocus()
                searchItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

}