package com.project.veganlife.mypage.data.datasource

import com.project.veganlife.data.model.ApiResult

interface MypageWithdrawalDataSource {
    suspend fun deleteWithdrawal(): ApiResult<Any>
}