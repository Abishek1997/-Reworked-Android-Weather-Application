package com.example.weatherApp.ui.weatherSearchResult

import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.MatrixCursor
import android.opengl.Visibility
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.Weatherapplication.R
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultFragmentDirections
import kotlinx.android.synthetic.main.activity_search_result_details.*
import kotlinx.android.synthetic.main.activity_search_result_details.toolbar
import kotlinx.android.synthetic.main.activity_weather_search_result.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class   WeatherSearchResultActivity : AppCompatActivity(), KodeinAware{

    override val kodein by closestKodein()
    private val viewModelFactory: WeatherSearchActivityViewmodelFactory by instance()
    private lateinit var viewModel: WeatherSearchActivityViewModel
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeSharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        themeSharedPreferences = applicationContext.getSharedPreferences("Theme", Context.MODE_PRIVATE)
        if (themeSharedPreferences.getString("theme", String()) == "light"){
            setTheme(R.style.ActivityLightTheme)
        } else{
            setTheme(R.style.ActivityThemeDark)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_search_result)
        setSupportActionBar(toolbar)
        sharedPreferences = applicationContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_search)
        bottomNav_home.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(WeatherSearchActivityViewModel::class.java)

        if (!viewModel.checkInternetConnection()){
            internet_summary_card.visibility = View.VISIBLE
        } else{
            internet_summary_card.visibility = View.GONE
        }

        refresh_button.setOnClickListener {
            this.recreate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu_item, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val suggestionAdapter: CursorAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null, arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1), intArrayOf(android.R.id.text1),
            0,
        )
        searchView.suggestionsAdapter = suggestionAdapter
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
                if (newText != null) {
                    val sAutocompleteColNames = arrayOf(
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1
                    )
                    if (newText.isNotEmpty()) {
                        runBlocking {
                            viewModel.getAutocompleteCitiesByQuery(newText).observe(
                                this@WeatherSearchResultActivity,
                                Observer { data ->
                                    val cursor = MatrixCursor(sAutocompleteColNames)
                                    for (index in data.predictions.indices) {
                                        val term: String = data.predictions[index]
                                        val row = arrayOf<Any>(index, term)
                                        cursor.addRow(row)
                                    }
                                    searchView.suggestionsAdapter.changeCursor(cursor)
                                })
                        }

                    } else {
                        searchView.suggestionsAdapter.changeCursor(null)
                    }
                }
                return false
            }
        })
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                val cursor: Cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val term: String =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                cursor.close()
                searchView.setQuery(term, false)
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                return onSuggestionSelect(position)
            }
        })
        return true
    }

    @SuppressLint("ApplySharedPref")
    override fun onDestroy() {
        super.onDestroy()
    }
}