package com.project.veganlife.lifecheck.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LifeCheckMealDataDetail(
    val id: Long,
    val name: String,
    val type: String,
    val amount: Int,
    val amountPerServe: Int,
    val caloriePerUnit: Double,
    val carbsPerUnit: Double,
    val proteinPerUnit: Double,
    val fatPerUnit: Double,
    val intakeUnit: String
) : Parcelable
