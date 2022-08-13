package org.soma.everyonepick.common.data.repository

import org.soma.everyonepick.common.data.dto.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserRepository {
    @GET("api/user/me")
    suspend fun readUser(@Header("Authorization") token: String): UserResponse
}