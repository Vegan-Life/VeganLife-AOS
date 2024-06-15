package com.project.veganlife.mypage.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.remote.MypageGetUserInfoApi
import java.lang.Exception
import javax.inject.Inject

class MypageGetUserInfoDataSourceImpl @Inject constructor(
    private val mypageGetUserInfoApi: MypageGetUserInfoApi,
    private val accessToken: SharedPreferences,
): MypageGetUserInfoDataSource{
    override suspend fun getUserInfo(): ApiResult<ProfileResponse> {
        val gson = GsonBuilder().create()

        return try {
            val profileInfoGetResponse = mypageGetUserInfoApi.getUserInfo(
                accessToken.getString("ApiAccessToken", null),
            )
            if (profileInfoGetResponse.isSuccessful == true) {
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