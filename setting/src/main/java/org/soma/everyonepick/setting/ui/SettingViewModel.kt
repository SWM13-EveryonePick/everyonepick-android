package org.soma.everyonepick.setting.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.common.data.entity.User

class SettingViewModel: ViewModel() {
    var me = MutableLiveData(User.dummyData)
    var isApiLoading = MutableLiveData(false)
}