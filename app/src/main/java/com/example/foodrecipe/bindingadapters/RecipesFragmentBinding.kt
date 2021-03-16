package com.example.foodrecipe.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foodrecipe.data.database.entity.RecipeEntity
import com.example.foodrecipe.modals.FoodRecipe
import com.example.foodrecipe.util.NetworkResult

class RecipesFragmentBinding {

    companion object{

        @BindingAdapter("android:readApiResponse", "android:readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
                view: ImageView,
                apiResponse: NetworkResult<FoodRecipe?>?,
                database: List<RecipeEntity>?){

            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                view.visibility = View.VISIBLE
            }else if(apiResponse is NetworkResult.Loading){
                view.visibility = View.INVISIBLE
            }else if(apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            }

        }

        @BindingAdapter(
                "android:readApiResponseforTxtView",
                "android:readDatabaseforTxtView",
                requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
                textView: TextView,
                apiResponse: NetworkResult<FoodRecipe?>?,
                database: List<RecipeEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            }else if(apiResponse is NetworkResult.Loading){
                textView.visibility = View.INVISIBLE
            }else if(apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }
        }
    }
}