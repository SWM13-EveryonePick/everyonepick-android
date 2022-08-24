package org.soma.everyonepick.groupalbum.data.entity

import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList

data class GroupAlbumReadDetail(
    val id: Long,
    var title: String,
    var hostUserId: Long,
    val users: List<User?>,
    var photos: List<Photo>
) {
    fun toGroupAlbum(): GroupAlbum {
        return GroupAlbum(title, users)
    }

    fun toMemberModelList() = MemberModelList(
        users.map { user ->
            user?.let { MemberModel(user, isChecked = false, isCheckboxVisible = false) }
        }.toMutableList() as MutableList<MemberModel>
    )
}