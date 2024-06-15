package com.project.veganlife.mypage.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.domain.repository.MypageGetUserRespository
import javax.inject.Inject

class MypageGetUserInfoUsecase @Inject constructor(
    val mypageGetUserRespository: MypageGetUserRespository,
) {
    suspend operator fun invoke(): ApiResult<ProfileResponse> {
        return mypageGetUserRespository.getUserInfo()
    }
}