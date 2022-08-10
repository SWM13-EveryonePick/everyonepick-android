package org.soma.everyonepick.common.api

import org.soma.everyonepick.foundation.data.model.SignUpRequest
import org.soma.everyonepick.foundation.data.model.JwtResponse
import org.soma.everyonepick.foundation.data.model.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): JwtResponse

    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshDto: RefreshRequest): JwtResponse
}