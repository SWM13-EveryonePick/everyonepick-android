package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewModel: ViewModel() {
    val photoUrl = MutableLiveData<String>(null)
}