package com.project.veganlife.recipe.data.model

import com.google.gson.annotations.SerializedName

data class RecipeRequestDTO(
    @SerializedName("name") val recipeTitle: String,
    val recipeType: List<String>,
    val ingredients: List<String>,
    val descriptions: List<String>
)