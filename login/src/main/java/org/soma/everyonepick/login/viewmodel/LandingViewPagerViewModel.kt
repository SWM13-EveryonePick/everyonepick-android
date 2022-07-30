package org.soma.everyonepick.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LandingViewPagerViewModel: ViewModel() {
    val currentItem = MutableLiveData(0)
    val isApiLoading = MutableLiveData(false)
}