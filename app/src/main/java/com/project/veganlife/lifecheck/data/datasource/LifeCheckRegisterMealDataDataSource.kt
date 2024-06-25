package com.project.veganlife.lifecheck.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest

interface LifeCheckRegisterMealDataDataSource {
    suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<LifeCheckMealDataRequest?>
}