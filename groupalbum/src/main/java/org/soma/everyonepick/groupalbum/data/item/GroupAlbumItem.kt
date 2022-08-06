package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.GroupAlbumDao


class GroupAlbumItem(
    val groupAlbumDao: GroupAlbumDao,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        fun dummyData() = GroupAlbumItem(GroupAlbumDao(-1, "", 0), isChecked = false, isCheckboxVisible = false)
    }

    override fun equals(other: Any?) = super.equals(other)
}