package com.project.veganlife.alarm.data.remote

import com.project.veganlife.alarm.data.model.AlarmContent
import com.project.veganlife.data.model.PagingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlarmApi {
    @GET("sse/notifications")
    suspend fun getAlarmList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<AlarmContent>>
}