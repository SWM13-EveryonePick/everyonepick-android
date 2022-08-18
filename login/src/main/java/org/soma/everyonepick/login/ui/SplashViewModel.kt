package org.soma.everyonepick.login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * [SplashActivity]에서의 비동기 작업의 성공 및 실패 카운트 정보를 홀드합니다.
 */
class SplashViewModel: ViewModel() {
    val success = MutableLiveData(0)
    val failure = MutableLiveData(0)
}