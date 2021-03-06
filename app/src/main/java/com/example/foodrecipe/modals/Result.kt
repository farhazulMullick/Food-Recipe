package com.example.foodrecipe.modals


import com.example.foodrecipe.modals.ExtendedIngredient
import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("aggregateLikes")
        val aggregateLikes: Int,
        @SerializedName("cheap")
        val cheap: Boolean,
        @SerializedName("dairyFree")
        val dairyFree: Boolean,
        @SerializedName("extendedIngredients")
        val extendedIngredients: List<ExtendedIngredient>,
        @SerializedName("glutenFree")
        val glutenFree: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("imageType")
        val imageType: String,
        @SerializedName("license")
        val license: String,
        @SerializedName("likes")
        val likes: Int,
        @SerializedName("readyInMinutes")
        val readyInMinutes: Int,
        @SerializedName("sourceName")
        val sourceName: String,
        @SerializedName("sourceUrl")
        val sourceUrl: String,
        @SerializedName("spoonacularSourceUrl")
        val spoonacularSourceUrl: String,
        @SerializedName("summary")
        val summary: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("vegan")
        val vegan: Boolean,
        @SerializedName("vegetarian")
        val vegetarian: Boolean,
        @SerializedName("veryHealthy")
        val veryHealthy: Boolean,
)