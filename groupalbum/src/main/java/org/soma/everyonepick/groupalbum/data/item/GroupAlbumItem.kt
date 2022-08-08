package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.GroupAlbum


class GroupAlbumItem(
    val groupAlbum: GroupAlbum,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        val dummyData = GroupAlbumItem(GroupAlbum(-1, "", 0), isChecked = false, isCheckboxVisible = false)
    }

    override fun equals(other: Any?) = super.equals(other)
}