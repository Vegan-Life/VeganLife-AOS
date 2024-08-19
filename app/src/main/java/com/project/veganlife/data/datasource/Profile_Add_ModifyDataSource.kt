package com.project.veganlife.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface Profile_Add_ModifyDataSource {
    suspend fun add_modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse>
}