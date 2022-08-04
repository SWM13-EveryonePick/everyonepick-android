package org.soma.everyonepick.login.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.PreferencesDataStore
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivitySplashBinding
import org.soma.everyonepick.login.utility.Util

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        supportActionBar?.hide()

        val activity = this
        lifecycleScope.launch {
            val accessTokenFlow = PreferencesDataStore(baseContext).getAccessToken()
            accessTokenFlow.collectLatest { token ->
                if (token != null) {
                    // TODO: Try to get access token by refresh token
                    // 토큰을 얻는 데 성공했는가?(리프레시 토큰이 유효한가?)
                    if (false) {
                        Util.doKakaoLogin(baseContext, { _,_ ->
                            Util.startHomeActivity(activity)
                        }, { _,_ ->
                            // 액세스 토큰까지 얻었지만 카카오톡 로그인이 안 되는 상태
                            Toast.makeText(baseContext, "카카오톡 로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            startLoginActivity()
                        })
                    } else {
                        startLoginActivity()
                    }
                } else {
                    startLoginActivity()
                }
            }
        }
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