package com.project.veganlife.lifecheck.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse

interface LifeCheckMonthlyCalorieRepository {
    suspend fun getMonthlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse>
}