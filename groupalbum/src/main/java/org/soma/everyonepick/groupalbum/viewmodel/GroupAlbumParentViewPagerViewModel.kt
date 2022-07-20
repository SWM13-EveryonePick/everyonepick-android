package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.utility.GroupAlbumListMode

class GroupAlbumParentViewPagerViewModel: ViewModel() {
    // 현재 item index에 따라 우측 상단 버튼의 visibility가 달라집니다.
    // 예를 들어 index가 1(즉, 친구 목록)일 경우에는 '선택' 버튼이 사라집니다.
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)

    // 앨범 탭에서의 일반 및 선택 모드를 나타내며, GroupAlbumListFragment에서도 해당 값을 참조합니다.
    val groupAlbumListMode = MutableLiveData(GroupAlbumListMode.NORMAL_MODE.ordinal)
}