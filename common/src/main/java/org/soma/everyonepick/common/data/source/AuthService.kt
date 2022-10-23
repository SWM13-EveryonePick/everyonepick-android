package org.soma.everyonepick.common.data.source

import org.soma.everyonepick.common.data.dto.SignUpRequest
import org.soma.everyonepick.common.data.dto.JwtResponse
import org.soma.everyonepick.common.data.dto.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): JwtResponse

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body refreshRequest: RefreshRequest): JwtResponse
}