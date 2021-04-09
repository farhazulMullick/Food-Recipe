package com.example.foodrecipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foodrecipe.data.Repository
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import com.example.foodrecipe.data.database.entity.FoodJokeEntity
import com.example.foodrecipe.data.database.entity.RecipeEntity
import com.example.foodrecipe.modals.FoodJoke
import com.example.foodrecipe.modals.FoodRecipe
import com.example.foodrecipe.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /*ROOM - OFFLINE CACHING*/

    val readOfflineRecipe: LiveData<List<RecipeEntity>> =
        repository.local.readRecipes().asLiveData()
    val readFavouriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.local.readFavouriteRecipes().asLiveData()

    val readOfflineJokes: LiveData<List<FoodJokeEntity>> =
        repository.local.readFoodJoke().asLiveData()


    private fun insertRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch(IO) {
        repository.local.insertRecipes(recipeEntity)
    }

    fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity) = viewModelScope.launch(IO) {
        repository.local.insertFavouriteRecipes(favouritesEntity)
    }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch {
        repository.local.insertFoodJoke(foodJokeEntity)
    }

    fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) = viewModelScope.launch(IO) {
        repository.local.deleteFavouriteRecipe(favouritesEntity)
    }

    fun deleteAllFavouriteRecipes() = viewModelScope.launch(IO) {
        repository.local.deleteAllFavouriteRecipes()
    }

    /*RETROFIT*/

    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                /* add - to local data- base*/
                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke != null){
                    offlineCacheJokes(foodJoke)
                }

            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Recipes not found.")
            }

        } else {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                // add to database when response is successfull

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipe(foodRecipe)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }

        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipe(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }

        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipe(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipe(recipeEntity)
    }

    private fun offlineCacheJokes(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        return when {
            response.message().toString().contains("timeout", ignoreCase = true) ->
                NetworkResult.Error("Connection Time out")

            response.code() == 402 ->
                NetworkResult.Error("API access limit reached!")

            response.body()?.results.isNullOrEmpty() ->
                NetworkResult.Error("Recipe not found!")

            response.isSuccessful -> {
                val foodRecipe = response.body()
                NetworkResult.Success(foodRecipe!!)
            }

            else -> NetworkResult.Error(response.message())
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when {
            response.message().toString().contains("timeout", ignoreCase = true) ->
                NetworkResult.Error("Connection Time out")

            response.code() == 402 ->
                NetworkResult.Error("API access limit reached!")

            response.body()?.text.isNullOrEmpty() ->
                NetworkResult.Error("No Jokes found!")

            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke!!)
            }

            else -> NetworkResult.Error(response.message())
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }

}
