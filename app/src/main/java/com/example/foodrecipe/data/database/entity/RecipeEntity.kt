package com.example.foodrecipe.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.modals.FoodRecipe
import com.example.foodrecipe.util.Constants.Companion.RECIPE_TABLE

@Entity(tableName = RECIPE_TABLE)
class RecipeEntity(
        @ColumnInfo(name = "foodrecipe")
        var foodRecipe: FoodRecipe
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
