package com.project.veganlife.lifecheck.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse

interface LifeCheckDailyIntakeDataSource {

    suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse>
}