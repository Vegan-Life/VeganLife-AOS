package com.project.veganlife.data.remote

import com.project.veganlife.data.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProfileAdd_ModifyApi {
    @Multipart
    @PUT("members/profile")
    suspend fun add_modifyProfile(
        @Header("Authorization") accessToken: String?,
        @Part("request") ProfileRequestDTO: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ProfileResponse>
}