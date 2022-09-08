package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum

// ApiResultGroupAlbumReadDetail
data class GroupAlbumResponse(
    val message: String,
    val data: GroupAlbum,
    val timestamp: String
)