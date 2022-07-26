package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.GroupAlbumDao


// GroupAlbum Recycler View에 들어가는 Item 객체입니다.
class GroupAlbumItem(
    val groupAlbumDao: GroupAlbumDao,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        fun dummyData() = GroupAlbumItem(GroupAlbumDao(-1, "", 0), isChecked = false, isCheckboxVisible = false)
    }
}