package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.community.data.repositoryimpl.CommunityRepositoryImpl
import com.project.veganlife.data.model.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedsGetViewModel @Inject constructor(
    private val communityRepositoryImpl: CommunityRepositoryImpl,
) : ViewModel() {
    private val _feeds = MutableLiveData<ApiResult<Feeds>>()
    val feeds get() = _feeds

    init {
        getFeeds()
    }

    fun getFeeds() {
        viewModelScope.launch {
            _feeds.value = communityRepositoryImpl.getFeeds()
        }
    }

    fun getFeedsByTag(tag: String) {
        //TODO: 필터링 된 피드 리스트를 갖고있따가, 새로 필터링한 값이 기존의 피드리스트랑 다르다면 업데이트, 다르지 않다면 업데이트 x 추가해줘야
        viewModelScope.launch {
            _feeds.value = communityRepositoryImpl.getFeedsByTag(tag)
        }
    }

}
