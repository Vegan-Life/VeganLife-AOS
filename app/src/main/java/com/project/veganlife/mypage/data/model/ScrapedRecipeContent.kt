package com.project.veganlife.mypage.data.model

import com.google.gson.annotations.SerializedName

data class ScrapedRecipeContent(
    @SerializedName("id") val recipeId: Long,
    @SerializedName("name") val recipeName: String,
    val thumbnailUrl: String,
    val recipeTypes: List<String>,
    val author: RecipeAuthor,
    var isLiked: Boolean
)
