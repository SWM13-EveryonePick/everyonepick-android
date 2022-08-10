package org.soma.everyonepick.foundation.data.model

data class Jwt(
    val everyonepickAccessToken: String,
    val everyonepickRefreshToken: String
)
