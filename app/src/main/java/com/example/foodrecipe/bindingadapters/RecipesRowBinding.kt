package com.example.foodrecipe.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.foodrecipe.R

class RecipesRowBinding {

    companion object{

        @BindingAdapter("androidLoadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(view: ImageView, imageUrl: String){
            view.load(imageUrl){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        //fun to convert Int value of "aggregate likes" to String type.
        @JvmStatic
        @BindingAdapter("android:setNumberOfLikes")
        fun setNumberOfLikes(textView: TextView, likes: Int){
            textView.text = likes.toString()
        }

        @BindingAdapter("android:setNumberOfMinuites")
        @JvmStatic
        fun setNumberOfMinuites(textView: TextView, minuites: Int){
            textView.text = minuites.toString()
        }

        @BindingAdapter("android:applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> view.setTextColor(
                            ContextCompat.getColor(
                                    view.context,
                                    R.color.green
                            )
                    )

                    is ImageView -> view.setColorFilter(
                            ContextCompat.getColor(
                                    view.context,
                                    R.color.green
                            )
                    )
                }
            }
        }

    }
}