package org.soma.everyonepick.groupalbum.data.entity

// ApiResultListGroupAlbumReadListDto
data class GroupAlbumListResponse(
    val message: String,
    val data: List<GroupAlbumReadListDto>,
    val timestamp: String
)