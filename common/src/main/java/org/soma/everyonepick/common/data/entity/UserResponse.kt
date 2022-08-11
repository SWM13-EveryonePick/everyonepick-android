package org.soma.everyonepick.common.data.entity

data class UserResponse(
    val message: String,
    val data: User,
    val timestamp: Long
)