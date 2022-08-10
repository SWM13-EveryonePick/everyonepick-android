package org.soma.everyonepick.groupalbum.data.entity

import org.soma.everyonepick.common.data.entity.User

data class GroupAlbumDto(
    var title: String,
    val users: List<User?>
)