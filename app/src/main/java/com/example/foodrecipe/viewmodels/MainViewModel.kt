package com.example.foodrecipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foodrecipe.data.Repository
import com.example.foodrecipe.data.database.RecipeEntity
import com.example.foodrecipe.modals.FoodRecipe
import com.example.foodrecipe.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        val  repository: Repository,
        application: Application
): AndroidViewModel(application) {

    /*ROOM - OFFLINE CACHING*/

    val readOfflineRecipe: LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()

    fun insertRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch(IO) {
        repository.local.insertRecipes(recipeEntity)
    }


    /*RETROFIT*/

    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes (queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                // add to database when response is sucessfull

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipe(foodRecipe)
                }

            }catch (e: Exception){
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }

        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private fun offlineCacheRecipe(foodRecipe: FoodRecipe) {
        val recipeEntity =  RecipeEntity(foodRecipe)
        insertRecipe(recipeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        return when{
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

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }

}
