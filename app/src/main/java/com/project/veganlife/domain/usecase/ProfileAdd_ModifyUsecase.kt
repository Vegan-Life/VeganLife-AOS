package com.project.veganlife.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.repository.Profile_Add_ModifyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileAdd_ModifyUsecase @Inject constructor(
    private val profileAddModifyrepository: Profile_Add_ModifyRepository,
) {
    suspend operator fun invoke(
        profileModifyDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        return profileAddModifyrepository.add_modifyProfile(profileModifyDTO, profilePhotoMultipart)
    }
}