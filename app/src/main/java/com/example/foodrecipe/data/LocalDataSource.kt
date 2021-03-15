package com.example.foodrecipe.data

import com.example.foodrecipe.data.database.RecipeEntity
import com.example.foodrecipe.data.database.RecipesDao
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import org.jsoup.nodes.Entities
import javax.inject.Inject


class LocalDataSource @Inject constructor(
        private val recipesDao: RecipesDao
){

    suspend fun insertRecipes(recipesEntity: RecipeEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity){
        recipesDao.insertFavouriteRecipe(favouritesEntity)
    }

    fun readRecipes(): Flow<List<RecipeEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>{
        return recipesDao.readFavouriteRecipes()
    }

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity){
        recipesDao.deleteFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteAllFavouriteRecipes(){
        recipesDao.deleteAllFavouriteRecipe()
    }
}