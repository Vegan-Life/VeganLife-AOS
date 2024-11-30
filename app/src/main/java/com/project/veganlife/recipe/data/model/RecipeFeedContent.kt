package com.project.veganlife.recipe.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeFeedContent(
    val author: RecipeAuthor,
    val id: Long,
    val isLiked: Boolean,
    @SerializedName("name") val recipeTitle: String,
    val recipeTypes: List<String>,
    val thumbnailUrl: String
) : Parcelable