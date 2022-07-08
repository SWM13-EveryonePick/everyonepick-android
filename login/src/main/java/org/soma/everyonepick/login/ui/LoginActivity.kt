package org.soma.everyonepick.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import org.soma.everyonepick.foundation.utility.NATIVE_APP_KEY
import org.soma.everyonepick.login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        KakaoSdk.init(this, NATIVE_APP_KEY)
    }

    // 새로운 환경에서 개발을 할 때마다 아래 함수를 실행하여 해시를 얻고,
    // 이를 kakao developers에 등록해야 합니다.
    private fun printAppKeyHash() {
        var keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash)
    }
}