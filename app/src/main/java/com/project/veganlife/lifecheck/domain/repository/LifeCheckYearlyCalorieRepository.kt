package com.project.veganlife.lifecheck.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse

interface LifeCheckYearlyCalorieRepository {
    suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse>
}