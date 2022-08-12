package org.soma.everyonepick.common.data.dto

import org.soma.everyonepick.common.data.entity.Jwt

// ApiResultJwt
data class JwtResponse(
    val message: String,
    val data: Jwt,
    val timestamp: Long
)