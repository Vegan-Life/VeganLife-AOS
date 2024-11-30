package com.project.veganlife.recipe.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeAuthor(
    val id: Int,
    val nickname: String,
    val vegetarianType: String
) : Parcelable
