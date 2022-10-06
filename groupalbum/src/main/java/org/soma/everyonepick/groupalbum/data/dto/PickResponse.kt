package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.Pick

data class PickResponse(
    val message: String,
    val data: Pick,
    val timestamp: Long
)