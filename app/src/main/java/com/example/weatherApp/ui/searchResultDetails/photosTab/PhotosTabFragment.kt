package com.example.weatherApp.ui.searchResultDetails.photosTab

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Weatherapplication.R
import com.example.weatherApp.data.network.pojos.DailyData
import com.example.weatherApp.data.network.pojos.ImagesResponse
import com.example.weatherApp.ui.customcoroutines.ScopedFragment
import com.example.weatherApp.ui.helpers.RecyclerViewItem
import com.example.weatherApp.ui.helpers.RecyclerViewItemPhotosFragment
import com.example.weatherApp.ui.searchResultDetails.SearchResultDetailsViewModel
import com.example.weatherApp.ui.searchResultDetails.SearchResultDetailsViewModelFactory
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModel
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.weather_search_result_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PhotosTabFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: SearchResultDetailsViewModelFactory by instance()
    private lateinit var viewModel: SearchResultDetailsViewModel

    companion object {
        fun newInstance() =
            PhotosTabFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photos_tab_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchResultDetailsViewModel::class.java)
        bindUI()
    }
    private fun bindUI() = launch {
        val currentWeather = viewModel.currentWeather.await()
        currentWeather.observe(viewLifecycleOwner, Observer{
            if (it == null) {
                group_ready.visibility = View.GONE
                group_loading.visibility = View.VISIBLE
                return@Observer
            }
        })

        val cityImages = viewModel.cityImages.await()

        cityImages.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
            setRecyclerView(it.images.createRecyclerViewItems())
        })
    }
    private fun List<String>.createRecyclerViewItems(): List<RecyclerViewItemPhotosFragment>{
        return this.map {
            RecyclerViewItemPhotosFragment(it, requireContext())
        }
    }

    private fun setRecyclerView(items: List<RecyclerViewItemPhotosFragment>){
        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply{
            layoutManager = LinearLayoutManager(this@PhotosTabFragment.context)
            adapter = groupieAdapter
        }
    }
}