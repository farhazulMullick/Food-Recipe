package com.example.foodrecipe.data

import com.example.foodrecipe.data.network.FoodRecipesApi
import com.example.foodrecipe.modals.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipe(queries: Map<String, String>): Response<FoodRecipe>{
        return foodRecipesApi.searchRecipe(queries)
    }
}