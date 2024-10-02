package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.domain.repository.CommunityRepository
import javax.inject.Inject

class SaveRecentSearchesUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(recentSearches: List<String>) {
        repository.saveRecentSearches(recentSearches)
    }
}