package org.soma.everyonepick.login.data.model

data class Jwt(
    val everyonepickAccessToken: String,
    val everyonepickRefreshToken: String
)
