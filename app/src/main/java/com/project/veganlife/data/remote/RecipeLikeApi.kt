package com.project.veganlife.data.remote

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeLikeApi {
    @POST("recipes/{id}/likes")
    suspend fun setRecipeLike(
        @Header("Authorization") accessToken: String?,
        @Path("id") id: Long
    ): Response<String>


}