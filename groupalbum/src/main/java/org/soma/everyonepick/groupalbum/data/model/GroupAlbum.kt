package org.soma.everyonepick.groupalbum.data.model

data class GroupAlbum(
    val id: Long,
    var title: String,
    var hostUserId: Long,
    var photoCount: Int
)