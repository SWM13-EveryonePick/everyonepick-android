package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.GroupAlbumListFragment
import org.soma.everyonepick.groupalbum.util.SelectionMode

class HomeViewPagerViewModel: ViewModel() {
    /**
     * 현재 item index에 따라 우측 상단 버튼의 visibility가 달라집니다.
     * 예를 들어 index가 1(즉, 친구 목록)일 경우에는 '선택' 버튼이 사라집니다.
     */
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)

    // 앨범 탭에서의 일반 및 선택 모드를 나타내며, GroupAlbumListFragment에서도 해당 값을 참조합니다.
    val selectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)

    /**
     * 전체선택 버튼을 누를 때 해당 값을 증가시키며, 하위 프래그먼트에서 이 값을 관찰합니다.
     * 이 값이 변경되면 [GroupAlbumListFragment]에서 전체 선택에 대한 액션을 수행하게 됩니다.
     */
    val checkAllTrigger = MutableLiveData(0)
}