package com.project.veganlife.lifecheck.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse

interface LifeCheckMonthlyCalorieDataSource {
    suspend fun getMonthlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse>
}