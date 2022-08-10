package org.soma.everyonepick.foundation.data.model

data class UserResponse(
    val message: String,
    val data: User,
    val timestamp: Long
)