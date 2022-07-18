package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.utility.GroupAlbumMode

class GroupAlbumViewPagerViewModel: ViewModel() {
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)

    // 앨범 탭에서의 일반 및 선택 모드를 나타내며, GroupAlbumFragment에서도 해당 값을 참조합니다.
    val groupAlbumMode: MutableLiveData<Int> = MutableLiveData(GroupAlbumMode.NORMAL_MODE.ordinal)
}