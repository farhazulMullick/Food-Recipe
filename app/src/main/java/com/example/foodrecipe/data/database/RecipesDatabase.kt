package com.example.foodrecipe.data.database

import android.content.Context
import androidx.room.*
import com.example.foodrecipe.util.Constants.Companion.DATABASE_NAME

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipeTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}