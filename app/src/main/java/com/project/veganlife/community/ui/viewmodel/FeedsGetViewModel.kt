package com.project.veganlife.community.ui.viewmodel

import android.content.SharedPreferences
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
    private val _feeds = MutableLiveData<Feeds>()
    val feeds get() = _feeds

    init {
        getFeeds()
    }

    private fun getFeeds() {
        viewModelScope.launch {
            _feeds.value = communityRepositoryImpl.getFeeds()
        }
    }
}
