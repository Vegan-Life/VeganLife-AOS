package com.project.veganlife.mypage.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.datasource.ProfileModifyDataSource
import com.project.veganlife.mypage.domain.repository.ProfileModifyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileModifyRepositoryImpl @Inject constructor(
    private val profileAddModifydatasource: ProfileModifyDataSource,
) : ProfileModifyRepository {

    override suspend fun modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        return profileAddModifydatasource.modifyProfile(profileRequestDTO, profilePhotoMultipart)
    }
}