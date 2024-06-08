package com.project.veganlife.lifecheck.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse

interface LifeCheckRecommendedIntakeRepository {
    suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>
}