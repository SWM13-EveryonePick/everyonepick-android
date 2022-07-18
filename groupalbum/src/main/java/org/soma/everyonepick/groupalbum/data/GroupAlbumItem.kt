package org.soma.everyonepick.groupalbum.data

// GroupAlbum Recycler View에 들어가는 Item 객체입니다.
data class GroupAlbumItem(
    val groupAlbum: GroupAlbum,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
)