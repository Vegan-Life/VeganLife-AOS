package com.project.veganlife.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.data.remote.ProfileAdd_ModifyApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class Profile_Add_ModifyDataSourceImpl @Inject constructor(
    private val profileaddModifyapi: ProfileAdd_ModifyApi,
    private val accessToken: SharedPreferences,
) : Profile_Add_ModifyDataSource {
    override suspend fun add_modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        val gson = GsonBuilder().create()

        return try {
            val token = accessToken.getString("ApiAccessToken", null)

            if (token == null) {
                return ApiResult.Error("mypage_dataSourceImpl", "AccessToken Null")
            }

            val profileInfoGetResponse = profileaddModifyapi.add_modifyProfile(
                token,
                profileRequestDTO,
                profilePhotoMultipart
            )

            if (profileInfoGetResponse.isSuccessful) {
                val responseBody = profileInfoGetResponse.body()!!
                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = profileInfoGetResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}