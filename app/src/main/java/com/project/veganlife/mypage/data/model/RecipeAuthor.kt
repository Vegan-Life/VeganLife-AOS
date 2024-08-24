package com.project.veganlife.mypage.data.model

import com.google.gson.annotations.SerializedName

data class RecipeAuthor(
    @SerializedName("id") val authorId: Long,
    val nickname: String,
    val vegetarianType: String
)
