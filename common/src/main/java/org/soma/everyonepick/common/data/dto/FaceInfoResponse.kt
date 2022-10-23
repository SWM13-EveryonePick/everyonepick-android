package org.soma.everyonepick.common.data.dto

import org.soma.everyonepick.common.data.entity.FaceInfo

// ApiResultFaceInfoDto
data class FaceInfoResponse(
    val message: String,
    val data: FaceInfo,
    val timestamp: Long
)
