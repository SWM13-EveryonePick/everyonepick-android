package org.soma.everyonepick.groupalbum.data.entity

import org.soma.everyonepick.common.data.entity.User

data class GroupAlbumReadList(
    val id: Long,
    var title: String,
    var hostUserId: Long,
    val users: List<User?>,
    var photoCnt: Int,
)
