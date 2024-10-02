package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.domain.usecase.GetPopularTagsUseCase
import com.project.veganlife.data.model.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunitySearchViewModel @Inject constructor(
    private val getPopularTagsUseCase: GetPopularTagsUseCase
) : ViewModel() {
    private val _popularTagList = MutableLiveData<ApiResult<PopularTagsResponse>>()
    val popularTagList: LiveData<ApiResult<PopularTagsResponse>> = _popularTagList

    init {
        loadPopularTags()
    }

    var pageStatus = MutableLiveData<PageStatus>(PageStatus.BEFORE)

    fun loadPopularTags() {
        viewModelScope.launch {
            _popularTagList.value = getPopularTagsUseCase.execute()
        }
    }
}

enum class PageStatus {
    BEFORE, DURING, AFTER
}