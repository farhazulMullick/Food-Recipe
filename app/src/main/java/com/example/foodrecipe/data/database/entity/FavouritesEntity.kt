package com.example.foodrecipe.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.modals.Result
import com.example.foodrecipe.util.Constants.Companion.FAVOURITE_RECIPES_TABLE

@Entity(tableName = FAVOURITE_RECIPES_TABLE)
data class FavouritesEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        var result: Result
)