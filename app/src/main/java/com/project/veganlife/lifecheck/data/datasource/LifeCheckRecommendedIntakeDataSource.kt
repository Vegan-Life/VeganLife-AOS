package com.project.veganlife.lifecheck.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse

interface LifeCheckRecommendedIntakeDataSource {
    suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>
}