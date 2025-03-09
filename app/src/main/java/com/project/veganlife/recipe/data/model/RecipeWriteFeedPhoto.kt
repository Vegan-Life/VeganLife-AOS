package com.project.veganlife.recipe.data.model

data class RecipeWriteFeedPhoto(
    val number: Int,
    val photo: String, // 기존 사진이면 URL, 추가한 사진이면 URI.toString()
    val isExisting: Boolean // 기존 사진 여부를 나타내는 플래그
)
