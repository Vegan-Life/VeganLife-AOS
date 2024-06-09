package com.project.veganlife.mypage.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.mypage.data.model.MyPostedContent
import kotlinx.coroutines.flow.Flow

interface MypagePostedRepository {
    suspend fun getMyPosted(type: String): Flow<PagingData<MyPostedContent>>
}
