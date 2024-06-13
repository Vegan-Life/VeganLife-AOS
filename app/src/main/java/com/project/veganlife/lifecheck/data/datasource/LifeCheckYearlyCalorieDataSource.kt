package com.project.veganlife.lifecheck.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse

interface LifeCheckYearlyCalorieDataSource {
    suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse>
}