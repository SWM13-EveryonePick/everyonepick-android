package org.soma.everyonepick.login.ui.landing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LandingViewPagerViewModel: ViewModel() {
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
}