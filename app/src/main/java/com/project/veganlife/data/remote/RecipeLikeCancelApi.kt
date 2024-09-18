package com.project.veganlife.data.remote

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface RecipeLikeCancelApi {
    @DELETE("recipes/{id}/likes")
    suspend fun setRecipeLikeCencel(
        @Path("id") id: Long
    ): Response<String>
}