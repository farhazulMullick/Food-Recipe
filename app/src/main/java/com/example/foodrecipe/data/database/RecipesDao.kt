package com.example.foodrecipe.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import com.example.foodrecipe.modals.Result

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("SELECT * FROM RECIPE_TABLE")
    fun readRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM favourite_recipe_table")
    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("DELETE FROM favourite_recipe_table ")
    suspend fun deleteAllFavouriteRecipe()
}