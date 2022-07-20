package org.soma.everyonepick.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    val isApiLoading = MutableLiveData(false)
}