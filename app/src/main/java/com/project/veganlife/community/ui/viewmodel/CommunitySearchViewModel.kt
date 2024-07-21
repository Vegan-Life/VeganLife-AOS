package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunitySearchViewModel: ViewModel() {
    var pageStatus = MutableLiveData<PageStatus>(PageStatus.BEFORE)


}

enum class PageStatus {
    BEFORE, DURING, AFTER
}