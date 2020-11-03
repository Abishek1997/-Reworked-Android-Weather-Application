package com.example.weatherApp.ui.weatherSearchResult.favorites

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Weatherapplication.R
import com.example.weatherApp.ui.customcoroutines.ScopedFragment
import com.example.weatherApp.ui.helpers.FavoritesRecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_weather_search_result.*
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FavoritesFragment : ScopedFragment(), FavoritesRecyclerView.AdapterListener, KodeinAware {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override val kodein by closestKodein()
    private val viewModelFactory: FavoritesViewModelFactory by instance()
    private lateinit var viewModel: FavoritesViewModel

    private lateinit var sharedPreferences: SharedPreferences
    private val favoritesRecyclerViewItems = ArrayList<FavoritesRecyclerView>()
    private var groupieAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireContext().getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FavoritesViewModel::class.java)
        favorites_text.setTypeface(null, Typeface.BOLD)
        bindUI()
    }

    private fun bindUI() = launch{
        val allEntries: Map<String?, *> = sharedPreferences.all
        val activityToolbarText = activity?.toolbar_title
        activityToolbarText!!.visibility = View.GONE

        for (key in allEntries.keys) {
            if (key != "initData"){
                favoritesRecyclerViewItems.add(FavoritesRecyclerView(key!!, requireContext(), this@FavoritesFragment))
            }
        }
        setFavoritesRecyclerView(favoritesRecyclerViewItems)
    }

    private fun setFavoritesRecyclerView(items: ArrayList<FavoritesRecyclerView>?){
         groupieAdapter = GroupAdapter<GroupieViewHolder>().apply {
            if (items != null) {
                addAll(items)
            }
        }
        favorites_recyclerView.apply{
            layoutManager = LinearLayoutManager(this@FavoritesFragment.context)
            adapter = groupieAdapter
        }

        groupieAdapter.setOnItemClickListener{ item, view ->
            (item as? FavoritesRecyclerView).let {
                launch(Dispatchers.IO) {
                    viewModel.setQueryToRepository(it!!.city)
                }
                val actionToCurrent = FavoritesFragmentDirections.actionCurrent()
                Navigation.findNavController(view).navigate(actionToCurrent)
            }
        }
    }

    override fun onClickItem(position: Int){
        Log.d("data", "${groupieAdapter.getItem(position)}")
        favoritesRecyclerViewItems.remove(groupieAdapter.getItem(position))
        setFavoritesRecyclerView(favoritesRecyclerViewItems)
    }
}