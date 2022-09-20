package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.Photo

// ApiResultListPhotoResponseDto
data class PhotoListResponse(
    val message: String,
    val data: List<Photo>,
    val timestamp: String
)