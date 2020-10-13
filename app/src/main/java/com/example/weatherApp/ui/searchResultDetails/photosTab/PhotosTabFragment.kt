package com.example.weatherApp.ui.searchResultDetails.photosTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.Weatherapplication.R

class PhotosTabFragment : Fragment() {

    companion object {
        fun newInstance() =
            PhotosTabFragment()
    }

    private lateinit var viewModel: PhotosTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photos_tab_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotosTabViewModel::class.java)
        // TODO: Use the ViewModel
    }

}