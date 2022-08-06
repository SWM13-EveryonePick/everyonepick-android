package org.soma.everyonepick.groupalbum.util

/**
 * Mode는 ViewPager2 내부 프래그먼트 단위로 다르지만, ViewPager2에서도 버튼을 통해
 * Mode값을 변경시킬 수 있으므로, ViewPager2의 ViewModel에서 값을 관리합니다.
 * 이에 따라 내부 Fragment에서 Parent ViewModel을 참조하여 observe하게 됩니다.
 */
enum class GroupAlbumListMode {
    NORMAL_MODE, SELECTION_MODE
}