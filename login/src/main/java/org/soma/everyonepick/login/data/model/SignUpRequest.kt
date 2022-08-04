package org.soma.everyonepick.login.data.model

import com.google.gson.annotations.SerializedName

// ProviderToken
data class SignUpRequest(
    val providerName: String,
    val providerAccessToken: String
)
