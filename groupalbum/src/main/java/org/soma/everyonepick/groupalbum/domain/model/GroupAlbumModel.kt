package org.soma.everyonepick.groupalbum.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum

class GroupAlbumModel(
    val groupAlbum: GroupAlbum,
    var isChecked: MutableStateFlow<Boolean>,
    var isCheckboxVisible: Boolean
) {
    companion object {
        fun createDummyData() = GroupAlbumModel(GroupAlbum(-1, "", 0, listOf(), 0), isChecked = MutableStateFlow(false), isCheckboxVisible = false)
    }

    // DiffCall의 areContentsTheSame()에서의 오류를 해결하기 위함
    // override fun equals(other: Any?) = super.equals(other)
    override fun equals(other: Any?): Boolean {
        return other is GroupAlbumModel && groupAlbum == other.groupAlbum && isChecked == other.isChecked && isCheckboxVisible == other.isCheckboxVisible
    }
}