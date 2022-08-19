package org.soma.everyonepick.login.ui.landing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LandingViewPagerViewModel: ViewModel() {
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _isApiLoading = MutableStateFlow(false)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun setIsApiLoading(flag: Boolean) {
        _isApiLoading.value = flag
    }
}