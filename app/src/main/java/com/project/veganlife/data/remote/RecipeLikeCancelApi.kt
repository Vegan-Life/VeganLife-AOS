package com.project.veganlife.data.remote

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeLikeCancelApi {
    @DELETE("recipes/{id}/likes")
    suspend fun setRecipeLikeCencel(
        @Header("Authorization") accessToken: String?,
        @Path("id") id: Long
    ): Response<String>


}