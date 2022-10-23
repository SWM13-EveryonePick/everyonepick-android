package org.soma.everyonepick.login.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivitySplashBinding
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    @Inject lateinit var homeActivityClass: Class<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // SDK가 S 미만인 경우에는 setContentView()로 그려지는 뷰를 스플래시 화면으로서 보여줍니다.
            binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
            supportActionBar?.hide()
        }

        subscribeUi()
        tryToAutoLogin()
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest {
                if (it.isNotEmpty()) {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 디바이스에 저장된 리프레시 토큰이 있는 경우, 'refresh token API 호출'과 '카카오톡 로그인'을 동시에 진행합니다.
     * 두 작업이 모두 성공한 경우에는 자동 로그인이 성공한 것이고, 둘 중 하나라도 실패하면 실패한 것입니다. 두 작업은 병렬적으로
     * 수행되므로 이를 체킹하기 위해서 [SplashViewModel.success], [SplashViewModel.failure]를 관찰합니다.
     */
    private fun tryToAutoLogin() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // 하나라도 실패했을 경우
                    viewModel.failure.collectLatest {
                        if (it >= 1) startLoginActivityAndFinish()
                    }
                }

                launch {
                    // 모두 성공했을 경우
                    viewModel.success.collectLatest {
                        if (it == 2) LoginUtil.startHomeActivity(this@SplashActivity, homeActivityClass)
                    }
                }
            }
        }

        viewModel.withRefreshToken { refreshToken ->
            if (refreshToken != null) {
                viewModel.refreshAccessTokenAndCheckIsUserRegistered(refreshToken)
                loginWithKakao()
            } else {
                startLoginActivityAndFinish()
            }
        }
    }

    private fun loginWithKakao() {
        LoginUtil.loginWithKakao(this, { _, _ ->
            viewModel.addSuccess()
        }, { _, _ ->
            Toast.makeText(baseContext, getString(R.string.toast_failed_to_login_with_kakao), Toast.LENGTH_SHORT).show()
            viewModel.addFailure()
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