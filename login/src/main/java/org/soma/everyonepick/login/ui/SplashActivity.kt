package org.soma.everyonepick.login.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.PreferencesDataStore
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivitySplashBinding
import org.soma.everyonepick.login.utility.LoginUtil

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        supportActionBar?.hide()

        lifecycleScope.launch { tryToLogin() }
    }

    /**
     * 아래의 순차적으로 발생하는 세 조건이 모두 만족될 경우 바로 HomeActivity로 이동하게 됩니다.(자동 로그인)
     * 1. 디바이스에 저장된 리프레시 토큰이 있으면서
     * 2. 리프레시 토큰이 유효하여 액세스 토큰을 얻을 수 있고
     * 3. 카카오톡 로그인에 성공할 경우
     * 조건이 맞지 않을 경우 LoginActivity로 이동합니다.
     */
    private suspend fun tryToLogin() {
        val refreshTokenFlow = PreferencesDataStore(baseContext).refreshToken
        refreshTokenFlow.collectLatest { token ->
            // 디바이스에 토큰이 저장되어 있는가?
            if (token != null) tryToGetAccessToken(token)
            else startLoginActivity()
        }
    }

    private suspend fun tryToGetAccessToken(refreshToken: String) {
        try {
            // TODO: val res = authService.getAccessToken(refreshToken)
            val d = 1/0 // TODO: 제거할 것. 오류를 유도하기 위한 코드. 현재는 리프레시 api가 없으므로 무조건 실패해야 함.
            PreferencesDataStore(baseContext).editAccessToken("TODO: 받아온 액세스 토큰")

            // 액세스 토큰 호출에 성공
            tryToLoginWithKakao()
        } catch (e: Exception) {
            // Refresh Token 만료
            startLoginActivity()
            Toast.makeText(baseContext, "Refresh Token이 유효하지 않습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tryToLoginWithKakao() {
        LoginUtil.loginWithKakao(baseContext, { _, _ ->
            // 로그인 성공
            LoginUtil.startHomeActivity(this)
        }, { _,_ ->
            // 로그인 실패
            startLoginActivity()
            Toast.makeText(baseContext, "카카오톡 로그인에 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        })
    }

    private fun startLoginActivity() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }


    override fun onStart() {
        super.onStart()
        window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            it.statusBarColor = Color.TRANSPARENT
        }
    }
}