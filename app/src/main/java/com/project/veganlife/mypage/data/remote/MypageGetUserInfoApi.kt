package com.project.veganlife.mypage.data.remote

import com.project.veganlife.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MypageGetUserInfoApi {
    @GET("members/profile")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String?,
    ): Response<ProfileResponse>
}