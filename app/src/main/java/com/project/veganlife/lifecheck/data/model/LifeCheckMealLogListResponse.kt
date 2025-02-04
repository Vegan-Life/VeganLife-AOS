package com.project.veganlife.lifecheck.data.model

data class LifeCheckMealLogListResponse(
    val id: Long,
    val mealType: String,
    val thumbnailUrl: String?,
    val totalCalorie: Int
)
