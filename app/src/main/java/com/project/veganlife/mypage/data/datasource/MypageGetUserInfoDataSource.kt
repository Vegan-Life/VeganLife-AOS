package com.project.veganlife.mypage.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse

interface MypageGetUserInfoDataSource {
    suspend fun getUserInfo(): ApiResult<ProfileResponse>
}