package org.soma.everyonepick.common.data.dto

// ProviderToken
data class SignUpRequest(
    val providerName: String,
    val providerAccessToken: String
)
