package com.project.veganlife.recipe.data.model

import com.google.gson.annotations.SerializedName

data class RecipeFeedContent(
    val author: RecipeAuthor,
    val id: Int,
    val isLiked: Boolean,
    @SerializedName("name") val recipeTitle: String,
    val recipeTypes: List<String>,
    val thumbnailUrl: String
)