package com.project.veganlife.mypage.domain.repository

import com.project.veganlife.data.model.ApiResult

interface MypageWithdrawalRespository {
    suspend fun deleteWithdrawal(): ApiResult<Any>
}