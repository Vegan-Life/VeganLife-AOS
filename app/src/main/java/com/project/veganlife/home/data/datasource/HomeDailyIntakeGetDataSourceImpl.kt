package com.project.veganlife.home.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.remote.DailyIntakeGetApi
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class HomeDailyIntakeGetDataSourceImpl @Inject constructor(
    private val dailyIntakeGetApi: DailyIntakeGetApi,
) : HomeDailyIntakeGetDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getDailyIntake(): ApiResult<DailyIntakeResponse>? {
        val gson = GsonBuilder().create()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.now().format(formatter)

        return try {
            val response = dailyIntakeGetApi.getDailyIntake(
                date.toString()
            )
            if (response?.isSuccessful == true) {
                val responseBody = response.body()!!

                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = response?.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}
