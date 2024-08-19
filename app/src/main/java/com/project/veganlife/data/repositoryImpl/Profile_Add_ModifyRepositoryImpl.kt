package com.project.veganlife.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.data.datasource.Profile_Add_ModifyDataSource
import com.project.veganlife.domain.repository.Profile_Add_ModifyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class Profile_Add_ModifyRepositoryImpl @Inject constructor(
    private val profileAddModifydatasource: Profile_Add_ModifyDataSource,
) : Profile_Add_ModifyRepository {

    override suspend fun add_modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        return profileAddModifydatasource.add_modifyProfile(profileRequestDTO, profilePhotoMultipart)
    }
}