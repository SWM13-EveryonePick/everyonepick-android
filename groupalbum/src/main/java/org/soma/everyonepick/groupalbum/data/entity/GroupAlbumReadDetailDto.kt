package org.soma.everyonepick.groupalbum.data.entity

import org.soma.everyonepick.common.data.entity.User

data class GroupAlbumReadDetailDto(
    val id: Long,
    var title: String,
    var hostUserId: Long,
    val users: List<User?>,
    var photos: List<Photo>
)

fun GroupAlbumReadDetailDto.toGroupAlbumDto(): GroupAlbumDto {
    return GroupAlbumDto(title, users)
}