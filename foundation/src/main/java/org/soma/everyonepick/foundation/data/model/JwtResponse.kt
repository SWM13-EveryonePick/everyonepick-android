package org.soma.everyonepick.foundation.data.model

// ApiResultJwt
data class JwtResponse(
    val message: String,
    val data: Jwt,
    val timestamp: Long
)