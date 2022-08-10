package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.GroupAlbum


class GroupAlbumItem(
    val groupAlbum: GroupAlbum,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        val dummyData = GroupAlbumItem(GroupAlbum(-1, "", 0, listOf(), 0), isChecked = false, isCheckboxVisible = false)
    }

    // DiffCall의 areContentsTheSame()에서의 오류를 해결하기 위함
    override fun equals(other: Any?) = super.equals(other)
}