package org.soma.everyonepick.common.data.model

import com.google.gson.annotations.SerializedName

// ApiResultJwt
data class JwtResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Jwt,
    @SerializedName("timestamp")
    val timestamp: Long
)