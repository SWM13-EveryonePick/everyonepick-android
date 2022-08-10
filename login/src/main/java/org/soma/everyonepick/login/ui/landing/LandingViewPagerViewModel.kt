package org.soma.everyonepick.login.ui.landing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LandingViewPagerViewModel: ViewModel() {
    val currentPosition = MutableLiveData(0)
    val isApiLoading = MutableLiveData(false)
}