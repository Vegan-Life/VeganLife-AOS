package com.project.veganlife.mypage.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.datasource.MypageGetUserInfoDataSource
import com.project.veganlife.mypage.domain.repository.MypageGetUserRespository
import javax.inject.Inject

class MypageGetUserInfoRepositoryImpl @Inject constructor(
    private val mypageGetUserInfoDataSource: MypageGetUserInfoDataSource,
) : MypageGetUserRespository {
    override suspend fun getUserInfo(): ApiResult<ProfileResponse> {
        return mypageGetUserInfoDataSource.getUserInfo()
    }
}