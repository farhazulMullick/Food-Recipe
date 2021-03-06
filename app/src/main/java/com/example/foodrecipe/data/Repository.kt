package com.example.foodrecipe.data

import com.example.foodrecipe.data.database.RecipesDao
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

// Annotated with @ActivityRetatinedScoped to inject in ViewModels

@ActivityRetainedScoped
class Repository @Inject constructor(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
) {

    val remote = remoteDataSource
    val local = localDataSource
}