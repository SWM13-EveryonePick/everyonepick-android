package org.soma.everyonepick.login.ui.landing

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.dto.SignUpRequest
import org.soma.everyonepick.common.data.entity.ProviderName
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.data.source.AuthService
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.FriendUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.login.R
import javax.inject.Inject

@HiltViewModel
class LandingViewPagerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService,
    private val userUseCase: UserUseCase,
    private val friendUseCase: FriendUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    private val _viewPagerPosition = MutableStateFlow(0)
    val viewPagerPosition: StateFlow<Int> = _viewPagerPosition

    private val _isApiLoading = MutableStateFlow(false)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    fun setViewPagerPosition(position: Int) {
        _viewPagerPosition.value = position
    }

    fun setIsApiLoading(flag: Boolean) {
        _isApiLoading.value = flag
    }

    fun signUp(token: OAuthToken?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val data = authService.signUp(SignUpRequest(ProviderName.KAKAO.name, token?.accessToken.toString())).data
                dataStoreUseCase.editAccessToken(data.everyonepickAccessToken)
                dataStoreUseCase.editRefreshToken(data.everyonepickRefreshToken)

                onSuccess.invoke()
            } catch (e: Exception) {
                _toastMessage.value = ""
                _toastMessage.value = context.getString(R.string.toast_failed_to_sign_up)
                _isApiLoading.value = false
            }
        }
    }

    fun withReadUser(onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val data = userUseCase.readUser(token)
                onSuccess.invoke(data)
            } catch (e: Exception) {
                _toastMessage.value = ""
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_user)
                _isApiLoading.value = false
            }
        }
    }

    fun readFriends(onAlways: () -> Unit) {
        friendUseCase.readFriends(onAlways)
    }
}