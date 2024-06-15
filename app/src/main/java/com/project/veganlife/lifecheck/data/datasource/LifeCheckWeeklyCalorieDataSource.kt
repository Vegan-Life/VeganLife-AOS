package com.project.veganlife.lifecheck.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse

interface LifeCheckWeeklyCalorieDataSource {
    suspend fun getWeeklyCalorie(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse>
}