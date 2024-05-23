package com.project.veganlife.mypage.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.mypage.data.datasource.MypageWithdrawalDataSource
import com.project.veganlife.mypage.domain.repository.MypageWithdrawalRespository
import javax.inject.Inject

class MypageWithdrawalRepositoryImpl @Inject constructor(
    private val mypageWithdrawalDataSource: MypageWithdrawalDataSource,
) : MypageWithdrawalRespository {
    override suspend fun deleteWithdrawal(): ApiResult<Any> {
        return mypageWithdrawalDataSource.deleteWithdrawal()
    }
}