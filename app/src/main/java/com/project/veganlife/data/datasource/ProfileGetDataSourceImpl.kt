package com.project.veganlife.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.data.remote.ProfileInfoGetApi
import java.lang.Exception
import javax.inject.Inject

class ProfileGetDataSourceImpl @Inject constructor(
    private val profileInfoGetApi: ProfileInfoGetApi,
    private val accessToken: SharedPreferences,
): ProfileGetDataSource{
    override suspend fun getInformation(): ApiResult<ProfileResponse> {
        val gson = GsonBuilder().create()

        return try {
            val profileInfoGetResponse = profileInfoGetApi.getInformation(
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