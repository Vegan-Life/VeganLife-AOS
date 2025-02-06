package com.project.veganlife.recipe.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeDetailContent(
    @SerializedName("name") val recipeTitle: String,
    val recipeTypes: List<String>,
    val imageUrls: List<String>,
    val ingredients: List<String>,
    val descriptions: List<String>,
    val author: RecipeAuthor,
    val isLiked: Boolean
): Parcelable