package org.soma.everyonepick.groupalbum.data.entity

import org.soma.everyonepick.common.data.entity.User

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
}