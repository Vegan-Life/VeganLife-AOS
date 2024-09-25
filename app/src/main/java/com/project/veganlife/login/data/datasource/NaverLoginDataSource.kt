package com.project.veganlife.login.data.datasource

import android.content.Context
import android.util.Log
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

    override suspend fun logout():String {
        return suspendCoroutine { continuation ->
            NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    //서버에서 토큰 삭제에 성공한 상태입니다.
                    continuation.resume("로그아웃")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                    // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                    Log.d("naver Logout", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                    Log.d("naver Logout", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                    continuation.resume("로그아웃 실패")
                }

                override fun onError(errorCode: Int, message: String) {
                    // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                    // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                    onFailure(errorCode, message)
                    continuation.resume("로그아웃 실패")
                }
            })
        }
    }

    override fun getUserInfo() {

    }
}