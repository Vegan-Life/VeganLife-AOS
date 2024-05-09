package com.project.veganlife.home.data.datasource

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.remote.ProfileInfoGetApi
import java.lang.Exception
import javax.inject.Inject

class HomeHomeProfilePhotoGetDataSourceImpl @Inject constructor(
    private val profileInfoGetApi: ProfileInfoGetApi,
    private val accessToken: SharedPreferences,
) : HomeProfilePhotoGetDataSource {
    override suspend fun getProfilePhoto(): String? {
        try {
            val profileInfoGetResponse = profileInfoGetApi.getInformation(
                accessToken.getString("ApiAccessToken", null),
            )
            return if (profileInfoGetResponse.code() == 200) {
                Log.d("HomeProfileInfoGetSuccess", profileInfoGetResponse.code().toString())
                profileInfoGetResponse.body()?.imageUrl
            } else {
                val errorBody = profileInfoGetResponse.errorBody()?.string()
                val conflictResponse = Gson().fromJson(errorBody, ConflictResponse::class.java)
                conflictResponse.description
            }
        } catch (e: Exception) {
            Log.e("HomeProfileInfoGetException", e.toString())
            return null
        }
    }
}