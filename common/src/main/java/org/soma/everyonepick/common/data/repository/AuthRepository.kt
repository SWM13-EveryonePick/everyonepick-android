package org.soma.everyonepick.common.data.repository

import org.soma.everyonepick.common.data.entity.SignUpRequest
import org.soma.everyonepick.common.data.entity.JwtResponse
import org.soma.everyonepick.common.data.entity.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepository {
    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): JwtResponse

    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshDto: RefreshRequest): JwtResponse
}