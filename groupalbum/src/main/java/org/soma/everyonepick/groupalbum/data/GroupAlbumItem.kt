package org.soma.everyonepick.groupalbum.data

data class GroupAlbumItem(
    val groupAlbum: GroupAlbum,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
)