package org.soma.everyonepick.groupalbum.data.entity

data class PickDetail(
    val id: Long,
    val isDone: Boolean,
    val expiredAt: String, // $date-time
    val photos: List<Photo>
)
