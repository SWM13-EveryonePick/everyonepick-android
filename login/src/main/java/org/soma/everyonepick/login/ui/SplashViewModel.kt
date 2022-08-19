package org.soma.everyonepick.login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [SplashActivity]에서의 비동기 작업의 성공 및 실패 카운트 정보를 홀드합니다.
 */
class SplashViewModel: ViewModel() {
    private val _success = MutableStateFlow(0)
    var success: StateFlow<Int> = _success

    private val _failure = MutableStateFlow(0)
    var failure: StateFlow<Int> = _failure

    fun addSuccess() {
        _success.value += 1
    }

    fun addFailure() {
        _failure.value += 1
    }
}