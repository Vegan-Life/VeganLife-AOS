package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.domain.usecase.GetRecentSearchesUseCase
import com.project.veganlife.community.domain.usecase.SaveRecentSearchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchesViewModel @Inject constructor(
    private val saveRecentSearchesUseCase: SaveRecentSearchesUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase
): ViewModel() {

    private val _recentSearchList = MutableLiveData<List<String>>(emptyList())
    val recentSearchList: LiveData<List<String>> = _recentSearchList

    init {
        loadStringList()
    }

    fun saveStringList(newSearch: String) {
        // 현재 값이 null인 경우 빈 리스트를 반환
        val currentList = recentSearchList.value ?: emptyList()

        // 새로운 값을 추가한 새로운 리스트 생성
        val updatedList = currentList.toMutableList().apply {
            add(newSearch)
        }

        // 업데이트된 리스트로 StateFlow 값을 설정
        _recentSearchList.value = updatedList

        // UseCase를 호출하여 데이터를 저장
        viewModelScope.launch {
            saveRecentSearchesUseCase.execute(updatedList)
        }
    }

    fun loadStringList() {
        viewModelScope.launch {
            getRecentSearchesUseCase.execute().collect {
                _recentSearchList.value = it
            }
        }
    }
}