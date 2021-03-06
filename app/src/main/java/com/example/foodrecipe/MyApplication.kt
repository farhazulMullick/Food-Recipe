package com.example.foodrecipe

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
Application level dependency container class for hilt
*/

@HiltAndroidApp
class MyApplication: Application() {
}