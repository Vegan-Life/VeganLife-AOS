package com.project.veganlife.mypage.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse

interface MypageGetUserRespository {
    suspend fun getUserInfo(): ApiResult<ProfileResponse>
}