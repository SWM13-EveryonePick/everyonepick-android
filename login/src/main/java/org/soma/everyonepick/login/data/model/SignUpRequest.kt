package org.soma.everyonepick.login.data.model

// ProviderToken
data class SignUpRequest(
    val providerName: String,
    val providerAccessToken: String
)
