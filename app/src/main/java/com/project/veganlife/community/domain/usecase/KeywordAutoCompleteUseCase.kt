package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class KeywordAutoCompleteUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(keyword: String, size: Int): ApiResult<List<String>> {
        return repository.getKeywordAutoComplete(keyword, size)
    }
}