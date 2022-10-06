package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.PhotoId


// ApiResultListPhotoDto
data class PhotoIdListResponse(
    val message: String,
    val data: List<PhotoId>,
    val timestamp: Long
)