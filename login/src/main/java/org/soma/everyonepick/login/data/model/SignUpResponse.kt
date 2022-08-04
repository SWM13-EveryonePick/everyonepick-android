package org.soma.everyonepick.login.data.model

// ApiResultJwt
data class SignUpResponse(
    val message: String,
    val data: Jwt,
    val timestamp: Int
)