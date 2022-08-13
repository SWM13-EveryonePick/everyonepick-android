package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetail

// ApiResultGroupAlbumReadDetail
data class GroupAlbumResponse(
    val message: String,
    val data: GroupAlbumReadDetail,
    val timestamp: String
)