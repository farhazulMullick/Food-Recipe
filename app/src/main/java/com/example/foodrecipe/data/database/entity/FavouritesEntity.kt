package com.example.foodrecipe.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.modals.Result
import com.example.foodrecipe.util.Constants.Companion.FAVOURITES_TABLE

@Entity(tableName = FAVOURITES_TABLE)
data class FavouritesEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var favouriteResults: Result
)