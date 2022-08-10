package org.soma.everyonepick.groupalbum.data.entity

data class GroupAlbumListResponse(
    val message: String,
    val data: List<GroupAlbum>,
    val timestamp: String
)