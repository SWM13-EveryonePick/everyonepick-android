package org.soma.everyonepick.login.data.model

data class UserResponse(
    val message: String,
    val data: User,
    val timestamp: Long
)