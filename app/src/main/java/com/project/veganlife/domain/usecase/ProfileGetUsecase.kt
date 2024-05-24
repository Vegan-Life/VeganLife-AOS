package com.project.veganlife.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.repository.ProfileGetRespository
import javax.inject.Inject

class ProfileGetUsecase @Inject constructor(
    val profileGetRespository: ProfileGetRespository
) {
    suspend operator fun invoke(): ApiResult<ProfileResponse> {
        return profileGetRespository.getInformation()
    }
}