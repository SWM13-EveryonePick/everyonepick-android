package org.soma.everyonepick.login.api

import org.soma.everyonepick.login.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("api/user/me")
    suspend fun getUser(@Header("Authorization") token: String): UserResponse
}