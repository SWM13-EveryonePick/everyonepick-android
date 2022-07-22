package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewModel: ViewModel() {
    val photoUrl = MutableLiveData<String>(null)
}