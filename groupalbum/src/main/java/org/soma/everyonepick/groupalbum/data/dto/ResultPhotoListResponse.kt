package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.ResultPhoto

data class ResultPhotoListResponse(
    val message: String,
    val data: List<ResultPhoto>,
    val timestamp: Long
)
