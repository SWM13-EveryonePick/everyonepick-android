package org.soma.everyonepick.login

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import org.soma.everyonepick.foundation.NATIVE_APP_KEY
import org.soma.everyonepick.login.databinding.ActivityLoginBinding
import java.security.MessageDigest
import java.util.*

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