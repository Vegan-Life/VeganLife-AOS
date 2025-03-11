package com.project.veganlife.mypage.data.model

import com.google.gson.annotations.SerializedName

data class RecipeRegister_Modify(
    @SerializedName("name") val recipeTitle: String,
    val recipeType: List<String>,
    val ingredients: List<String>,
    val descriptions: List<String>
)
