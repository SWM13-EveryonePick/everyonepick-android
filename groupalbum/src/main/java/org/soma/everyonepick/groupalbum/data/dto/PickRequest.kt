package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.PhotoId

data class PickRequest(
    val timeOut: Long, // 초 단위
    val photos: List<PhotoId>
)