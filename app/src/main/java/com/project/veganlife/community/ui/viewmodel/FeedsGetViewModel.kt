package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.community.data.repositoryimpl.CommunityRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedsGetViewModel @Inject constructor(
    private val communityRepositoryImpl: CommunityRepositoryImpl,
) : ViewModel() {
    private val _feedsGet = MutableLiveData<Feeds>()
    val feedsGet get() = _feedsGet

    fun getFeeds(accessToken: String) {
        viewModelScope.launch {
            _feedsGet.value = communityRepositoryImpl.getFeeds(accessToken)
        }
    }
}
