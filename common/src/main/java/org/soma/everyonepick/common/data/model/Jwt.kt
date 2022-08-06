package org.soma.everyonepick.common.data.model

data class Jwt(
    val everyonepickAccessToken: String,
    val everyonepickRefreshToken: String
)
