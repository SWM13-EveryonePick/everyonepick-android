package org.soma.everyonepick.groupalbum.data.model

import org.soma.everyonepick.foundation.data.model.User

data class GroupAlbum(
    val id: Long,
    var title: String,
    var hostUserId: Long,
    val users: List<User>,
    var photoCnt: Int
)
