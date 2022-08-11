package org.soma.everyonepick.groupalbum.data.entity

// ApiResultGroupAlbumReadDetailDto
data class GroupAlbumResponse(
    val message: String,
    val data: GroupAlbumReadDetailDto,
    val timestamp: String
)