package com.project.veganlife.lifecheck.data.model

data class LifeCheckMealLogDetailResponse(
    val id: Long,
    val mealType: String,
    val totalIntakeNutrients: LifeCheckIntakeNutrients,
    val meals: List<LifeCheckMealLogMeal>,
    val imageUrls: List<String>
)
