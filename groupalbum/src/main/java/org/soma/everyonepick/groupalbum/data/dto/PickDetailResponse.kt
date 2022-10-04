package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.PickDetail

data class PickDetailResponse(
    val message: String,
    val data: PickDetail,
    val timestamp: Long
)