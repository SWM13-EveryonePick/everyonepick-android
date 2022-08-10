package org.soma.everyonepick.common.api

import org.soma.everyonepick.foundation.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("api/user/me")
    suspend fun getUser(@Header("Authorization") token: String): UserResponse
}