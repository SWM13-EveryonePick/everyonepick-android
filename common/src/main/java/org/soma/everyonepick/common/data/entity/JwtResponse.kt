package org.soma.everyonepick.common.data.entity

// ApiResultJwt
data class JwtResponse(
    val message: String,
    val data: Jwt,
    val timestamp: Long
)