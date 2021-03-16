package com.example.foodrecipe.util

class Constants {

    companion object{
        const val BASE_URL = "https://api.spoonacular.com/"
        const val API_KEY= ""
        const val BASE_RECIPE_IMAGE_URL = "https://spoonacular.com/recipeImages/"
        const val SIZE_AND_TYPE = "-636x393.jpg"

        // Ingredient image url
        const val BASE_INGREDIENT_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_250x250/"

        // Query key constants
        const val QUERY = "query"
        const val QUERY_NUMBERS = "number"
        const val QUERY_API_KEY= "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"

        // Room Database
        const val DATABASE_NAME = "recipe_database"
        const val RECIPE_TABLE = "recipe_table"
        const val FAVOURITE_RECIPES_TABLE = "favourite_recipe_table"

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