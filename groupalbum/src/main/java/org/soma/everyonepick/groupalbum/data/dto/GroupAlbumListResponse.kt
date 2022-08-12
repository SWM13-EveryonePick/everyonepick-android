package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadList

// ApiResultListGroupAlbumReadListDto
data class GroupAlbumListResponse(
    val message: String,
    val data: List<GroupAlbumReadList>,
    val timestamp: String
)