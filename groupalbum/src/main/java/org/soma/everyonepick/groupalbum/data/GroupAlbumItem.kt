package org.soma.everyonepick.groupalbum.data

data class GroupAlbumItem(
    val groupAlbum: GroupAlbum,
    var isSelected: Boolean,
    var isCheckboxVisible: Boolean
)