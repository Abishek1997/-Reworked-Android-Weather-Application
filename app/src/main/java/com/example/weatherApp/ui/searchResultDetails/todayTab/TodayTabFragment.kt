package com.example.weatherApp.ui.searchResultDetails.todayTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R

class TodayTabFragment : Fragment() {

    companion object {
        fun newInstance() =
            TodayTabFragment()
    }

    private lateinit var viewModel: TodayTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.today_tab_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodayTabViewModel::class.java)
        // TODO: Use the ViewModel
    }

}