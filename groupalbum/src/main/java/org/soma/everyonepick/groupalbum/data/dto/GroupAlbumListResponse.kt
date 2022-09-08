package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum

// ApiResultListGroupAlbumReadListDto
data class GroupAlbumListResponse(
    val message: String,
    val data: List<GroupAlbum>,
    val timestamp: String
)