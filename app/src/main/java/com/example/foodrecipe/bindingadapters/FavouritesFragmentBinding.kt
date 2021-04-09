package com.example.foodrecipe.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.adapter.FavouritesAdapter
import com.example.foodrecipe.data.database.entity.FavouritesEntity

class FavouritesFragmentBinding {

    companion object {

        @BindingAdapter("android:viewVisibility", "android:setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favouritesEntity: List<FavouritesEntity>?,
            mAdapter: FavouritesAdapter?
        ) {
            if(favouritesEntity.isNullOrEmpty()){

                when(view){

                    is ImageView ->{
                        view.visibility = View.VISIBLE
                    }
                    is TextView->{
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }
            else{
                when(view){

                    is ImageView ->{
                        view.visibility = View.INVISIBLE
                    }
                    is TextView->{
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favouritesEntity)
                    }
                }
            }
        }

    }
}