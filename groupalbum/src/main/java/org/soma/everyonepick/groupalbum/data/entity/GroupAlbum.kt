package org.soma.everyonepick.groupalbum.data.entity

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList

data class GroupAlbum(
    val id: Long? = null,
    var title: String? = null,
    var hostUserId: Long? = null,
    val users: List<User?>? = null,
    var photoCnt: Int? = null
) {
    fun getMemberModelList() = MemberModelList(
        users?.map { user ->
            user?.let { MemberModel(user, MutableStateFlow(false), isCheckboxVisible = false) }
        }?.toMutableList() as MutableList<MemberModel>
    )
}