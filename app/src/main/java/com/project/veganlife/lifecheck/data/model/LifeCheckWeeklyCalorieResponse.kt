package com.project.veganlife.lifecheck.data.model

data class LifeCheckWeeklyCalorieResponse(
    val totalCalorie: Int,
    val periodicCalorie: List<LifeCheckCalorieDetail>
)

data class LifeCheckCalorieDetail(
    val breakfast: Int,
    val lunch: Int,
    val dinner: Int,
    val snack: Int
)