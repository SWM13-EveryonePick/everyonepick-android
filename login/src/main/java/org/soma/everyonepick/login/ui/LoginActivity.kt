package org.soma.everyonepick.login.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.foundation.utility.NATIVE_APP_KEY
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()

        KakaoSdk.init(this, NATIVE_APP_KEY)
    }

    /**
     * 새로운 환경에서 개발을 할 때마다 아래 함수를 실행하여 해시를 얻고,
     * Kakao Developers 계정주가 해당 해시를 등록해야 합니다.
     */
    private fun printAppKeyHash() {
        var keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash)
    }
}