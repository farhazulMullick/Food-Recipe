package com.example.foodrecipe.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import com.example.foodrecipe.data.database.entity.FoodJokeEntity
import com.example.foodrecipe.data.database.entity.RecipeEntity
import com.example.foodrecipe.util.Constants
@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM RECIPE_TABLE ORDER BY ID ASC")
    fun readRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM FAVOURITES_TABLE ORDER BY id ASC")
    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Query("SELECT * FROM FOODJOKE_TABLE ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    @Delete
    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("DELETE FROM FAVOURITES_TABLE ")
    suspend fun deleteAllFavouriteRecipe()
}