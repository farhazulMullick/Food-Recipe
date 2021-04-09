package com.example.foodrecipe.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodrecipe.data.Repository
import com.example.foodrecipe.data.database.RecipesDatabase
import com.example.foodrecipe.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
            @ApplicationContext context: Context,
    ): RecipesDatabase{
        return Room.databaseBuilder(
                context,
                RecipesDatabase::class.java,
                DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesDao(database: RecipesDatabase) = database.recipesDao()
}