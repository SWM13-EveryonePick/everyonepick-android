package org.soma.everyonepick.login.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.dto.RefreshRequest
import org.soma.everyonepick.common.data.source.AuthService
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

/**
 * [SplashActivity]에서의 비동기 작업의 성공 및 실패 카운트 정보를 홀드합니다.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService,
    private val userUseCase: UserUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _success = MutableStateFlow(0)
    val success: StateFlow<Int> = _success

    private val _failure = MutableStateFlow(0)
    val failure: StateFlow<Int> = _failure

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    fun addSuccess() {
        _success.value += 1
    }

    fun addFailure() {
        _failure.value += 1
    }

    fun withRefreshToken(callback: (String?) -> Unit) {
        viewModelScope.launch {
            callback.invoke(dataStoreUseCase.refreshToken.first())
        }
    }

    fun refreshAccessTokenAndCheckIsUserRegistered(refreshToken: String) {
        viewModelScope.launch {
            try {
                val data = authService.refresh(RefreshRequest(refreshToken)).data
                dataStoreUseCase.editAccessToken(data.everyonepickAccessToken)

                // 얼굴정보 등록 여부 체크
                val user = userUseCase.readUser(dataStoreUseCase.bearerAccessToken.first()!!)
                if (user.isRegistered == true) addSuccess()
                else {
                    dataStoreUseCase.removeAccessToken()
                    dataStoreUseCase.removeRefreshToken()
                    addFailure()
                }
            } catch (e: Exception) {
                _toastMessage.value = ""
                _toastMessage.value = context.getString(R.string.toast_failed_to_refresh_token)

                dataStoreUseCase.removeAccessToken()
                dataStoreUseCase.removeRefreshToken()
                addFailure()
            }
        }
    }
}