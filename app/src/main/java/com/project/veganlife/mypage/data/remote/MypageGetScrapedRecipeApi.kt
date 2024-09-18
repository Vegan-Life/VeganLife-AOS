package com.project.veganlife.mypage.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MypageGetScrapedRecipeApi {
    @GET("members/me/liked-recipes")
    suspend fun getMyScrapedRecipeList(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<PagingResponse<ScrapedRecipeContent>>
}