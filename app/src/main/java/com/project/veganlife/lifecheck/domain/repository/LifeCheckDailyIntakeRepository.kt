package com.project.veganlife.lifecheck.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse

interface LifeCheckDailyIntakeRepository {
    //repository interface, 데이터 get 메소드 정의
    // 날짜를 받아 일일 섭취량 응답 반환
    suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse>
}