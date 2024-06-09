package com.project.veganlife.mypage.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.domain.repository.MypagePostedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MypageGetPostedUsecase @Inject constructor(
    val mypagePostedRepository: MypagePostedRepository,
) {
    suspend operator fun invoke(type: String): Flow<PagingData<MyPostedContent>> {
        return mypagePostedRepository.getMyPosted(type)
    }
}