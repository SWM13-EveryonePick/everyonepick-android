package org.soma.everyonepick.login.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.soma.everyonepick.common.data.dto.RefreshRequest
import org.soma.everyonepick.common.data.repository.AuthRepository
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.NATIVE_APP_KEY
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivitySplashBinding
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        super.onCreate(savedInstanceState)
        // SDK가 S 이하인 경우에는 setContentView()로 그려지는 뷰를 스플래시 화면으로서 보여줍니다.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        supportActionBar?.hide()

        KakaoSdk.init(this, NATIVE_APP_KEY)
        tryToAutoLogin()
    }

    /**
     * 디바이스에 저장된 리프레시 토큰이 있는 경우, refresh token API 호출과 카카오톡 로그인 시도를 동시에 진행합니다.
     * 이때 각 작업이 모두 성공하면 [LoginUtil.startHomeActivity]를 실행하고, 하나라도 실패할 경우 작업을 취소하고
     * [LoginActivity]로 이동합니다.
     */
    private fun tryToAutoLogin() {
        val activity = this
        job?.cancel()
        job = lifecycleScope.launch {
            val refreshToken = dataStoreUseCase.refreshToken.first()
            if (refreshToken != null) {
                awaitAll(
                    async { refreshAccessTokenAndSaveToDataStore(refreshToken) },
                    async { loginWithKakao() }
                )

                // 모두 성공했을 경우
                viewModel.success.observe(activity) {
                    if (it == 2) LoginUtil.startHomeActivity(activity)
                }

                // 하나라도 실패했을 경우
                viewModel.failure.observe(activity) {
                    if (it >= 1) {
                        cancel()
                        startLoginActivityAndFinish()
                    }
                }
            } else {
                startLoginActivityAndFinish()
            }
        }
    }

    private suspend fun refreshAccessTokenAndSaveToDataStore(refreshToken: String) {
        try {
            val data = authRepository.refresh(RefreshRequest(refreshToken)).data
            dataStoreUseCase.editAccessToken(data.everyonepickAccessToken)

            viewModel.success.value = viewModel.success.value?.plus(1)
        } catch (e: Exception) {
            Toast.makeText(baseContext, "Refresh Token이 유효하지 않습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            viewModel.failure.value = viewModel.failure.value?.plus(1)
        }
    }

    private fun loginWithKakao() {
        LoginUtil.loginWithKakao(this, { _, _ ->
            viewModel.success.value = viewModel.success.value?.plus(1)
        }, { _, _ ->
            Toast.makeText(baseContext, "카카오톡 로그인에 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            viewModel.failure.value = viewModel.failure.value?.plus(1)
        })
    }

    private fun startLoginActivityAndFinish() {
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