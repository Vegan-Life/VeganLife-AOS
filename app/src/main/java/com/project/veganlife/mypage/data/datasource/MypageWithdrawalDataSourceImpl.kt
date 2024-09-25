package com.project.veganlife.mypage.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.mypage.data.remote.MypageWithDrawalApi
import java.lang.Exception
import javax.inject.Inject

class MypageWithdrawalDataSourceImpl @Inject constructor(
    private val mypageWithDrawalApi: MypageWithDrawalApi,
): MypageWithdrawalDataSource{
    override suspend fun deleteWithdrawal(): ApiResult<Any> {
        val gson = GsonBuilder().create()

        return try {
            val response = mypageWithDrawalApi.deleteWithdrawal(
            )
            if (response.isSuccessful == true) {
                val responseBody = response.code()

                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = response.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}