package org.soma.everyonepick.common.data.dto

import org.soma.everyonepick.common.data.entity.User

data class UserResponse(
    val message: String,
    val data: User,
    val timestamp: Long
)