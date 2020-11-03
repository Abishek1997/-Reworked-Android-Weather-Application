package com.example.weatherApp.ui.helpers
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.Weatherapplication.R
import com.example.weatherApp.internal.GlideApp
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.example.weatherApp.internal.PhotosFragmentGlideModule
import kotlinx.android.synthetic.main.recycler_view_photo_fragment.*

class RecyclerViewItemPhotosFragment(
    private val imageURL: String,
    private val context: Context
) : Item(){
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            GlideApp.with(context)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(recyclerView_city_image)
        }
    }

    override fun getLayout() = R.layout.recycler_view_photo_fragment
}