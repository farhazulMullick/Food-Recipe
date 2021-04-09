package com.example.foodrecipe.data.database

import androidx.room.*
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import com.example.foodrecipe.data.database.entity.FoodJokeEntity
import com.example.foodrecipe.data.database.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class, FavouritesEntity::class, FoodJokeEntity::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(RecipeTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}