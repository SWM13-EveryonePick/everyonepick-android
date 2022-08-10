package org.soma.everyonepick.groupalbum.data.model

data class GroupAlbumListResponse(
    val message: String,
    val data: List<GroupAlbum>,
    val timestamp: String
)