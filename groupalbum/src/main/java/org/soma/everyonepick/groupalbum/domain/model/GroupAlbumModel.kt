package org.soma.everyonepick.groupalbum.domain.model

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadListDto

class GroupAlbumModel(
    val groupAlbum: GroupAlbumReadListDto,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        val dummyData = GroupAlbumModel(GroupAlbumReadListDto(-1, "", 0, listOf(), 0), isChecked = false, isCheckboxVisible = false)
    }

    // DiffCall의 areContentsTheSame()에서의 오류를 해결하기 위함
    override fun equals(other: Any?) = super.equals(other)
}