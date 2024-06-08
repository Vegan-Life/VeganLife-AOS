package com.project.veganlife.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse

interface ProfileGetRespository {
    suspend fun getInformation(): ApiResult<ProfileResponse>
}