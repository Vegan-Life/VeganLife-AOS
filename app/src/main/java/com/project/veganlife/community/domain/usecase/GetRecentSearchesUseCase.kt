package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(private val repository: CommunityRepository) {
    fun execute(): Flow<List<String>> {
        return repository.getRecentSearches()
    }
}