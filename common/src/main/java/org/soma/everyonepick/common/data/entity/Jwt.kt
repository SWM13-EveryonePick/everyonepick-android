package org.soma.everyonepick.common.data.entity

data class Jwt(
    val everyonepickAccessToken: String,
    val everyonepickRefreshToken: String
)
