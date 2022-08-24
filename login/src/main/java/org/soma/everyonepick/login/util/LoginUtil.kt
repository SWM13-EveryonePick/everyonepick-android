package org.soma.everyonepick.login.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import org.soma.everyonepick.common.util.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.common.util.NATIVE_APP_KEY

class LoginUtil {
    companion object {
        fun startHomeActivity(activity: Activity) {
            val intent = Intent(activity, Class.forName(HOME_ACTIVITY_CLASS)).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(org.soma.everyonepick.common_ui.R.anim.slide_in_bottom, org.soma.everyonepick.common_ui.R.anim.stay_out);
        }

        fun loginWithKakao(
            context: Context,
            onLoginSuccess: (OAuthToken?, Throwable?) -> Unit,
            onLoginFailure: (OAuthToken?, Throwable?) -> Unit
        ) {
            KakaoSdk.init(context, NATIVE_APP_KEY)

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) onLoginFailure(token, error)
                else if (token != null) onLoginSuccess(token, error)
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        onLoginFailure(token, error)

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도한다.
                        // 단, 사용자가 의도적으로 로그인을 취소한 경우는 제외한다.
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            onLoginFailure(token, error)
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    }
                    else if (token != null) onLoginSuccess(token, error)
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }
}