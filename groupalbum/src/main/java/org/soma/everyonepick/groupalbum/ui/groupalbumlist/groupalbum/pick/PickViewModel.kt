package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PickViewModel: ViewModel() {
    private var _photoUrlList = MutableStateFlow<List<String>>(listOf())
    val photoUrlList: StateFlow<List<String>> = _photoUrlList

    fun setPhotoUrlList(data: List<String>) {
        _photoUrlList.value = data
    }
}