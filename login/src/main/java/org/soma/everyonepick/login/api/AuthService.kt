package org.soma.everyonepick.login.api

import org.soma.everyonepick.login.data.model.SignUpRequest
import org.soma.everyonepick.login.data.model.SignUpResponse
import org.soma.everyonepick.common.api.Retrofit2Factory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @POST("api/user/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse
}