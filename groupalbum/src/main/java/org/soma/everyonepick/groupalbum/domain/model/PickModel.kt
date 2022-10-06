package org.soma.everyonepick.groupalbum.domain.model

data class PickModel(
    val id: Long,
    val isDone: Boolean,
    val thumbnailUrl: String
)