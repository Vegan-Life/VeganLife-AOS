package com.project.veganlife.login.data.datasource

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class KakaoLoginDataSource @Inject constructor(@ApplicationContext val context: Context) :
    LoginDataSource {
    override suspend fun login(): String {

        return suspendCancellableCoroutine {
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(
                        "Login-kakao",
                        "카카오계정으로 로그인 실패", error
                    )
                    it.resume(null.toString(), {})
                } else if (token != null) {
                    Log.d(
                        "Login-kakao",
                        "카카오계정으로 로그인 성공 ${token.accessToken}"
                    )
                    it.resume(token.accessToken, {})
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        Log.e(
                            "Login-kakao",
                            "카카오톡으로 로그인 실패", error
                        )
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    } else if (token != null) {
                        Log.i(
                            "Login-kakao",
                            "카카오톡으로 로그인 성공 ${token.accessToken}"
                        )
                        it.resume(token.accessToken, {})
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }

        }
    }

    override fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(
                    "Login-kakao",
                    "사용자 정보 요청 실패", error
                )
            } else if (user != null) {
                Log.d(
                    "Login-kakao",
                    "사용자 정보 요청 성공"
                )
            }
        }
    }

    override suspend fun logout():String {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(
                        "Logout-kakao",
                        "로그아웃 실패. SDK에서 토큰 삭제됨", error
                    )
                    continuation.resume("로그아웃 실패")
                } else {
                    Log.i(
                        "Logout-kakao",
                        "로그아웃 성공. SDK에서 토큰 삭제됨"
                    )
                    continuation.resume("로그아웃")
                }
            }
        }

    }
}