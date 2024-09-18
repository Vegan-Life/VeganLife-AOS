package com.project.veganlife.data.remote

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeLikeApi {
    @POST("recipes/{id}/likes")
    suspend fun setRecipeLike(
        @Path("id") id: Long
    ): Response<String>
}