package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.PickInfo

data class PickInfoResponse(
    val message: String,
    val data: PickInfo,
    val timestamp: Long
)