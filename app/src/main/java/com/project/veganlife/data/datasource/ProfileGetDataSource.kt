package com.project.veganlife.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse

interface ProfileGetDataSource {
    suspend fun getInformation(): ApiResult<ProfileResponse>
}