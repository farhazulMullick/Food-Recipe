package com.example.foodrecipe.util

class Constants {

    companion object{
        const val BASE_URL = "https://api.spoonacular.com/"
        const val API_KEY= "9897f9f617c44d0d8c3b727ce92fa70a"

        // Query constants
        const val QUERY_NUMBERS = "number"
        const val QUERY_API_KEY= "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"

        // Room Database
        const val DATABASE_NAME = "recipe_database"
        const val RECIPE_TABLE = "recipe_table"

        // Bottom sheet and Preferences
        const val PREFERENCE_NAME = "food_Preferences"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_NUMBER = "50"
        const val PREFERENCE_MEAL_TYPE = "mealType"
        const val PREFERENCE_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCE_DIET_TYPE = "dietType"
        const val PREFERENCE_DIET_TYPE_ID = "dietTypeId"

        // Back online
        const val PREFERENCE_BACK_ONLINE = "backOnline"
    }
}