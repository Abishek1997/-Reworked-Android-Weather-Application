package com.example.weatherApp.ui.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.Weatherapplication.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.favorites_recycler_item.*

class FavoritesRecyclerView(
    val city: String,
    private val context: Context,
    private val adapterListener: AdapterListener
) : Item(){

    companion object {
        var clickListener: AdapterListener? = null
    }

    override fun getLayout() = R.layout.favorites_recycler_item
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            clickListener = adapterListener
            val recyclerViewCityText = favorites_recyclerView_cityName
            recyclerViewCityText.text = city
            sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
            favorites_recyclerView_mark_favorite.setOnClickListener {
                editor = sharedPreferences.edit()
                editor.remove(city)
                editor.apply()
                favorites_recyclerView_mark_favorite.setImageResource(R.drawable.favorites_unselected_icon)
                clickListener?.onClickItem(adapterPosition)
                val toast: Toast = Toast.makeText(
                    context,
                    "$city was removed from favorites",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }
    interface AdapterListener {
        fun onClickItem(position: Int)
    }
}