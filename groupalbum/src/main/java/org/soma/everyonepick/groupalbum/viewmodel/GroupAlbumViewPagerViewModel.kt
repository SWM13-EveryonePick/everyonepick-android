package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupAlbumViewPagerViewModel: ViewModel() {
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)
}