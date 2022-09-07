package org.soma.everyonepick.camera.data.dto

import org.soma.everyonepick.camera.data.entity.PosePack

data class PosePackListResponse(
    val message: String,
    val data: List<PosePack>,
    val timestamp: String
)