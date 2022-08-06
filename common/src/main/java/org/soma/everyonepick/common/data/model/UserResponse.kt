package org.soma.everyonepick.common.data.model

data class UserResponse(
    val message: String,
    val data: User,
    val timestamp: Long
)