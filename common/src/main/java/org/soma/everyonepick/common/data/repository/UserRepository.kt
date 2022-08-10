package org.soma.everyonepick.common.data.repository

import org.soma.everyonepick.foundation.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header
import javax.inject.Inject

interface UserRepository {
    @GET("api/user/me")
    suspend fun getUser(@Header("Authorization") token: String): UserResponse
}