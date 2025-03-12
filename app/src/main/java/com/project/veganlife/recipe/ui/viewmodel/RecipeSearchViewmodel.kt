package com.project.veganlife.recipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.domain.usecase.RecipeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSearchViewmodel @Inject constructor(
    val recipeUsecase: RecipeUsecase,
) : ViewModel() {
    // 최근 검색어
    private val _recentSearches = MutableLiveData<List<String>>(emptyList())
    val recentSearches: LiveData<List<String>> = _recentSearches

    // 검색어 결과
    private val _searchResults = MutableStateFlow<PagingData<RecipeFeedContent>>(PagingData.empty())
    val searchResults: StateFlow<PagingData<RecipeFeedContent>> = _searchResults

    init {
        loadRecentSearches()
    }

    // 🔹 최근 검색어 불러오기
    private fun loadRecentSearches() {
        viewModelScope.launch {
            recipeUsecase.getResentSearchRecipe()
                .collect { recent ->
                    _recentSearches.value = recent
                }
        }
    }

    // 🔹 검색 실행
    fun searchRecipe(newSearch: String) {
        if (newSearch.isBlank()) return

        // 현재 값이 null인 경우 빈 리스트를 반환
        val currentList = recentSearches.value ?: emptyList()

        // 새로운 값을 추가한 새로운 리스트 생성
        val updatedList = currentList.toMutableList().apply {
            remove(newSearch) // 중복 제거
            add(newSearch) }
        // 최근 검색어에 저장
        saveRecentSearch(updatedList.reversed())

        // 검색 결과
        viewModelScope.launch {
            recipeUsecase.getSearchRecipe(newSearch)
                .cachedIn(viewModelScope)
                .collect{ pagingData ->
                    _searchResults.value = pagingData
                }
        }
    }

    fun saveRecentSearch(newSearch: List<String>) {
        // 최근 검색어 저장
        viewModelScope.launch {
            recipeUsecase.saveResentSearchRecipe(newSearch)
        }
    }
}