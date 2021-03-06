package com.example.foodrecipe.data.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.foodrecipe.modals.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipetoString(foodRecipe: FoodRecipe): String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(jsonData: String): FoodRecipe{
        val turnsType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(jsonData, turnsType)
    }
}