package com.project.veganlife.mypage.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.data.remote.ProfileAdd_ModifyApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class ProfileModifyDataSourceImpl @Inject constructor(
    private val profileaddModifyapi: ProfileAdd_ModifyApi,
) : ProfileModifyDataSource {
    override suspend fun modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        val gson = GsonBuilder().create()

        return try {
            val response = profileaddModifyapi.add_modifyProfile(
                profileRequestDTO,
                profilePhotoMultipart
            )

            if (response.isSuccessful) {
                val responseBody = response.body()!!
                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = response.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}