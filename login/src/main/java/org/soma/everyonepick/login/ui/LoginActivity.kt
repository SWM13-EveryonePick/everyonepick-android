package org.soma.everyonepick.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.data.repository.AuthRepository
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivityLoginBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()
    }

    /**
     * 새로운 환경에서 개발을 할 때마다 Kakao Developers 계정주가 새로운 해시를 등록해야 합니다. 이 함수를 통해 해시값을
     * 얻고 계정주에게 해시 등록을 요청해야 합니다.
     */
    private fun printAppKeyHash() {
        var keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash)
    }
}