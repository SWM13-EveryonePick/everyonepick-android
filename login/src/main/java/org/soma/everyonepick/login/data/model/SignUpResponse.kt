package org.soma.everyonepick.login.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

// ApiResultJwt
data class SignUpResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Jwt,
    @SerializedName("timestamp")
    val timestamp: Long
)