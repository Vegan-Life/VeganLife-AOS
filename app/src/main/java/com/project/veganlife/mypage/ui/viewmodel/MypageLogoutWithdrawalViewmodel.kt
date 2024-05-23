package com.project.veganlife.mypage.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.mypage.domain.usecase.MypageLogoutUsecase
import com.project.veganlife.mypage.domain.usecase.MypageWithdrawalUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageLogoutWithdrawalViewmodel @Inject constructor(
    private val mypageLogoutUsecase: MypageLogoutUsecase,
    private val mypageWithdrawalUsecase: MypageWithdrawalUsecase,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {
    private val _logout = MutableLiveData<String>()
    val logout: LiveData<String> get() = _logout

    private val _withdrawal = MutableLiveData<Any>()
    val withdrawal: LiveData<Any> get() = _withdrawal

    fun doUserLogout() {
        viewModelScope.launch {
            val provider = sharedPreferences.getString("provider",null)
            _logout.value = provider?.let { mypageLogoutUsecase.invoke(it) }
        }
    }

    fun deleteWithDrawal() {
        viewModelScope.launch {
            _withdrawal.value = mypageWithdrawalUsecase.invoke()
        }
    }

}