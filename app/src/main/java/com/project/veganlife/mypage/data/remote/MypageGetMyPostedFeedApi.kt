package com.project.veganlife.mypage.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.mypage.data.model.MyPostedContent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MypageGetMyPostedFeedApi {
    @GET("members/me/posts")
    suspend fun getMyPostedFeedList(
        @Header("Authorization") accessToken: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<MyPostedContent>>
}