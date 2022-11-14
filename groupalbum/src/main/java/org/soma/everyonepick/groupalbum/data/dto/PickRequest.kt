package org.soma.everyonepick.groupalbum.data.dto

import org.soma.everyonepick.groupalbum.data.entity.PhotoId

data class PickRequest(
    val photos: List<PhotoId>
)