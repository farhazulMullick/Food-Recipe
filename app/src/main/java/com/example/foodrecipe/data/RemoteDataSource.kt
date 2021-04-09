package com.example.foodrecipe.data

import com.example.foodrecipe.data.network.FoodRecipesApi
import com.example.foodrecipe.modals.FoodJoke
import com.example.foodrecipe.modals.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipe(searchQuery: Map<String, String>): Response<FoodRecipe>{
        return foodRecipesApi.searchRecipe(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke>{
        return foodRecipesApi.getFoodJoke(apiKey)
    }
}