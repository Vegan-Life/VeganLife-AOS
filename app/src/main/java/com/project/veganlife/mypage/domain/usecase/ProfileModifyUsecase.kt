package com.project.veganlife.mypage.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.domain.repository.ProfileModifyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileModifyUsecase @Inject constructor(
    private val profileAddModifyrepository: ProfileModifyRepository,
) {
    suspend operator fun invoke(
        profileModifyDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        return profileAddModifyrepository.modifyProfile(profileModifyDTO, profilePhotoMultipart)
    }
}