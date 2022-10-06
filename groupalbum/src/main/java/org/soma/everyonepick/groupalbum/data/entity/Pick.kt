package org.soma.everyonepick.groupalbum.data.entity

data class Pick(
    val id: Long,
    val isDone: Boolean,
    val expiredAt: String, // $date-time
    val photo: Photo
)
