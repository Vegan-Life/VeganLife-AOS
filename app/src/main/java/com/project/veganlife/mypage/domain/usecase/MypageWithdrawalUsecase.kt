package com.project.veganlife.mypage.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.mypage.domain.repository.MypageWithdrawalRespository
import javax.inject.Inject

class MypageWithdrawalUsecase @Inject constructor(
    val withdrawalRespository: MypageWithdrawalRespository,
    ) {
    suspend operator fun invoke(): ApiResult<Any> {
        return withdrawalRespository.deleteWithdrawal()
    }
}