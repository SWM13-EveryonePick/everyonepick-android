package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.utility.PhotoListMode

class GroupAlbumViewPagerViewModel: ViewModel() {
    val groupAlbum = MutableLiveData(GroupAlbumDao(-1, "Loading"))

    val currentItem: MutableLiveData<Int> = MutableLiveData(0)
    val photoListMode = MutableLiveData(PhotoListMode.NORMAL_MODE.ordinal)
    // TODO: 합성중 / 합성완료 모드
}