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

    fun saveStringList(stringList: List<String>) {
        viewModelScope.launch {
            saveRecentSearchesUseCase.execute(stringList)
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