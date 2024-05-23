package com.project.veganlife.mypage.data.remote

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header

interface MypageWithDrawalApi {
    @DELETE("members")
    suspend fun deleteWithdrawal(
        @Header("Authorization") accessToken: String?,
    ): Response<Any>
}