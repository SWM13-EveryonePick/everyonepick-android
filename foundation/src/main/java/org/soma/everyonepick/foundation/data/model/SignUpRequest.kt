package org.soma.everyonepick.foundation.data.model

// ProviderToken
data class SignUpRequest(
    val providerName: String,
    val providerAccessToken: String
)
