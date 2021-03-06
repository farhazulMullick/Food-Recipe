package com.example.foodrecipe.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipeEntity)

    @Query("SELECT * FROM RECIPE_TABLE")
    fun readRecipes(): Flow<List<RecipeEntity>>
}