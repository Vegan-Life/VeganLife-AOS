package com.project.veganlife.login.ui.view.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.domain.usecase.LoginUsecase
import com.project.veganlife.login.domain.usecase.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUsecase,
) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse get() = _loginResponse

    suspend fun login(provider: String,context: Context) {
        viewModelScope.launch {
            val getAccessToken = loginUseCase(provider,context)
            _loginResponse.value = getAccessToken
        }
    }
}