package com.example.foodrecipe.data

import com.example.foodrecipe.data.database.RecipeEntity
import com.example.foodrecipe.data.database.RecipesDao
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

    fun readRecipes(): Flow<List<RecipeEntity>>{
        return recipesDao.readRecipes()
    }
}