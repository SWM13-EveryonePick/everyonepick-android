package org.soma.everyonepick.common.data.repository

import org.soma.everyonepick.common.data.dto.SignUpRequest
import org.soma.everyonepick.common.data.dto.JwtResponse
import org.soma.everyonepick.common.data.dto.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepository {
    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): JwtResponse

    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshRequest: RefreshRequest): JwtResponse
}