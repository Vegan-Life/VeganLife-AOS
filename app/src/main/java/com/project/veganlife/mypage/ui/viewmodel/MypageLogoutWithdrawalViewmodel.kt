package com.project.veganlife.mypage.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
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
    private val _logout = MutableLiveData<String>().apply { value = "" }
    val logout: LiveData<String> get() = _logout

    private val _withdrawal = MutableLiveData<Any>().apply { value = "" }
    val withdrawal: LiveData<Any> get() = _withdrawal

    fun doUserLogout(type: String) {
        viewModelScope.launch {
            val provider = sharedPreferences.getString("provider", null)
            provider?.let {
                when(type) {
                    "로그아웃" -> {
                        val logoutResponse = mypageLogoutUsecase.invoke(it)
                        _logout.value = logoutResponse // 로그아웃 결과를 LiveData에 설정
                    } else -> {
                        mypageLogoutUsecase.invoke(it)
                    }
                }
            }
        }
    }

    fun deleteWithDrawal() {
        viewModelScope.launch {
            val response = mypageWithdrawalUsecase.invoke()
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    _withdrawal.value = response
                    Log.d("withDrawal Error", responseDescription)
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "withDrawal Exception",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    _withdrawal.value = response.data.toString()
                }
            }
        }
    }
}