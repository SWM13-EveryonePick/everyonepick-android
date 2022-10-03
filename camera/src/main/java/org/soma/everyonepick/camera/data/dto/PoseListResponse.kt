package org.soma.everyonepick.camera.data.dto

import org.soma.everyonepick.camera.data.entity.Pose


data class PoseListResponse(
    val message: String,
    val data: List<Pose>,
    val timestamp: Long
)
