package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.utility.PhotoListMode

class GroupAlbumViewPagerViewModel: ViewModel() {
    // Fragment가 args를 통해 group album id를 가지고 있으므로, Fragment단에서 초기화를 진행합니다.
    val groupAlbum = MutableLiveData(GroupAlbumDao(-1, "Loading"))
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)

    val photoListMode = MutableLiveData(PhotoListMode.NORMAL_MODE.ordinal)
    // TODO: 합성중 / 합성완료 모드

    fun updateGroupAlbumTitle(newTitle: String) {
        val newGroupAlbum = groupAlbum.value?.copy(title = newTitle)
        groupAlbum.value = newGroupAlbum
    }
}