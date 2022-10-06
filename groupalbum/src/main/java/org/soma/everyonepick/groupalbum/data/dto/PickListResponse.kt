package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.Pick

data class PickListResponse(
    val message: String,
    val data: List<Pick>,
    val timestamp: Long
)
