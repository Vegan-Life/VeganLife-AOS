package com.project.veganlife.community.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.data.repositoryimpl.CommunityRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedsGetViewModel @Inject constructor(
    private val communityRepositoryImpl: CommunityRepositoryImpl,
) : ViewModel() {
    private val _postPreviewList = MutableStateFlow<PagingData<PostPreview>?>(null)
    val postPreviewList: StateFlow<PagingData<PostPreview>?> get() = _postPreviewList

    fun getFeeds() {
        viewModelScope.launch {
            try {
                communityRepositoryImpl.getFeeds()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _postPreviewList.value = pagingData
                    }
            } catch (e: Exception) {
                Log.e("##ERROR", "getFeeds: paging error, ${e.message}")
            }

        }
    }

    fun getFeedsByTag(tag: String) {
        viewModelScope.launch {
            try {
                communityRepositoryImpl.getFeedsByTag(tag)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _postPreviewList.value = pagingData
                    }
            } catch (e: Exception) {
                Log.e("##ERROR", "getFeeds: paging error, ${e.message}")
            }
        }
    }

}
