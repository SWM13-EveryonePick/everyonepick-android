package org.soma.everyonepick.login.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.NATIVE_APP_KEY
import org.soma.everyonepick.login.R
import org.soma.everyonepick.common.data.repository.AuthRepository
import org.soma.everyonepick.common.data.entity.RefreshRequest
import org.soma.everyonepick.login.databinding.ActivitySplashBinding
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        supportActionBar?.hide()

        KakaoSdk.init(this, NATIVE_APP_KEY)
        lifecycleScope.launch {
            tryToAutoLogin()
        }
    }

    /**
     * 아래의 순차적으로 발생하는 세 조건이 모두 만족될 경우 바로 HomeActivity로 이동하게 됩니다.(자동 로그인)
     * 1. 디바이스에 저장된 리프레시 토큰이 있는 경우
     * 2. 리프레시 토큰이 유효하여 액세스 토큰을 얻는 데 성공한 경우
     * 3. 카카오톡 로그인에 성공할 경우
     * 조건이 맞지 않을 경우에는 [LoginActivity]로 이동합니다.
     */
    private suspend fun tryToAutoLogin() {
        val refreshToken = dataStoreUseCase.refreshToken.first()
        if (refreshToken != null) {
            try {
                val data = authRepository.refresh(RefreshRequest(refreshToken)).data
                dataStoreUseCase.editAccessToken(data.everyonepickAccessToken)

                loginWithKakaoAndStartHomeActivity()
            } catch (e: Exception) {
                startLoginActivity()
                Toast.makeText(baseContext, "Refresh Token이 유효하지 않습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        else startLoginActivity()
    }

    private fun loginWithKakaoAndStartHomeActivity() {
        LoginUtil.loginWithKakao(this, { _, _ ->
            LoginUtil.startHomeActivity(this)
        }, { _,_ ->
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