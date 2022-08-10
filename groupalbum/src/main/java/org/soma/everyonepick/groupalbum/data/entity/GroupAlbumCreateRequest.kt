package org.soma.everyonepick.groupalbum.data.entity

import org.soma.everyonepick.common.data.entity.User

// GroupAlbumDto
data class GroupAlbumCreateRequest(
    val title: String,
    val users: List<User>
)