package com.project.veganlife.lifecheck.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest

interface LifeCheckRegisterMealDataRepository {
    suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<LifeCheckMealDataRequest?>
}