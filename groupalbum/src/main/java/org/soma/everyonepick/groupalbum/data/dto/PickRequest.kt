package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.PhotoId

data class PickRequest(
    val timeOut: Int,
    val photos: List<PhotoId>
)