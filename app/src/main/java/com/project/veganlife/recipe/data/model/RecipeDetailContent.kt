package com.project.veganlife.recipe.data.model

import com.google.gson.annotations.SerializedName

data class RecipeDetailContent(
    @SerializedName("name") val recipeTitle: String,
    val recipeTypes: List<String>,
    val imageUrls: List<String>,
    val ingredients: List<String>,
    val descriptions: List<String>,
    val author: RecipeAuthor,
    val isLiked: Boolean
)