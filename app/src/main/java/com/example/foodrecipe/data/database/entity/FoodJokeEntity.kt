package com.example.foodrecipe.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.modals.FoodJoke
import com.example.foodrecipe.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
data class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
