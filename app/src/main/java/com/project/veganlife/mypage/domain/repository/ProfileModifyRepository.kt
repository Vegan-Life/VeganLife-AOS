package com.project.veganlife.mypage.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileModifyRepository {
    suspend fun modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse>
}
