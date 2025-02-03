package com.project.veganlife.lifecheck.data.model

data class LifeCheckMealLogDTO(
    val intake: Int,
    val calorie: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val mealDataId: Long
)