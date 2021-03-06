package com.example.foodrecipe.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.example.foodrecipe.data.DataStoreRepository
import com.example.foodrecipe.util.Constants.Companion.API_KEY
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_NUMBER
import com.example.foodrecipe.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodrecipe.util.Constants.Companion.QUERY_API_KEY
import com.example.foodrecipe.util.Constants.Companion.QUERY_DIET
import com.example.foodrecipe.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foodrecipe.util.Constants.Companion.QUERY_NUMBERS
import com.example.foodrecipe.util.Constants.Companion.QUERY_TYPE
import com.example.foodrecipe.util.NetworkListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
        application: Application,
        private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun showNetworkStatus() {
        if (!networkStatus && !backOnline) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            writeBackOnline(true)
        }
        else if (networkStatus && backOnline) {

            Toast.makeText(getApplication(), "We are back Online", Toast.LENGTH_SHORT).show()
            writeBackOnline(false)
        }
    }

    fun writeMealAndDietType(
            mealType: String,
            mealTypeId: Int,
            dietType: String,
            dietTypeId: Int
    ) = viewModelScope.launch(IO) {

        dataStoreRepository.writeMealAndDietType(
                mealType,
                mealTypeId,
                dietType,
                dietTypeId
        )
    }

    fun writeBackOnline(backOnline: Boolean) {
        viewModelScope.launch(IO) {
            dataStoreRepository.writeBackOnline(backOnline)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val mQuery = HashMap<String, String>()

        viewModelScope.launch {
            readMealAndDietType.collect { preference ->
                mealType = preference.selectedMealType
                dietType = preference.selectedDietType
            }
        }

        mQuery[QUERY_NUMBERS] = DEFAULT_NUMBER
        mQuery[QUERY_API_KEY] = API_KEY
        mQuery[QUERY_TYPE] = mealType
        mQuery[QUERY_DIET] = dietType
        mQuery[QUERY_FILL_INGREDIENTS] = "true"
        mQuery[QUERY_ADD_RECIPE_INFORMATION] = "true"

        return mQuery
    }
}