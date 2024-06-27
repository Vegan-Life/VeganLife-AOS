package com.project.veganlife.lifecheck.data.model

data class LifeCheckMealDataRequest(
    val name: String,
    val amount: Int,
    val amountPerServe: Int,
    val caloriePerServe: Int,
    val carbsPerServe: Int,
    val proteinPerServe: Int,
    val fatPerServe: Int,
    val intakeUnit: String
)
