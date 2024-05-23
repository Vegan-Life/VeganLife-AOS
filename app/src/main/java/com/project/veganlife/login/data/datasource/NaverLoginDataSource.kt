package com.project.veganlife.login.data.datasource

import android.content.Context
import android.util.Log
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class NaverLoginDataSource @Inject constructor(@ApplicationContext val context: Context) :
    LoginDataSource {
    override suspend fun login(): String {
        return suspendCancellableCoroutine {
            val oauthLoginCallback =
                object : OAuthLoginCallback {
                    override fun onError(
                        errorCode: Int,
                        message: String,
                    ) {
                        onFailure(errorCode, message)
                        it.resume(null.toString(), {})
                    }

                    override fun onFailure(
                        httpStatus: Int,
                        message: String,
                    ) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Log.e(
                            "Login-naver",
                            "errorCode:$errorCode errorDescription:$errorDescription"
                        )
                        it.resume(null.toString(), {})
                    }

                    override fun onSuccess() {
                        Log.d("Login-naver", "로그인 성공")
                        it.resume(NaverIdLoginSDK.getAccessToken().toString(), {})
                    }
                }
            NaverIdLoginSDK.logout()
            CoroutineScope(Dispatchers.Main).launch {
                // UI 스레드에서 호출되어야 하는 작업
                NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
            }
        }
    }

    override fun logout():String {
        NaverIdLoginSDK.logout()
        return "로그아웃"
    }

    override fun getUserInfo() {

    }
}