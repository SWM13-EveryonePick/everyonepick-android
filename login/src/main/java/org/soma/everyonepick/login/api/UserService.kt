package org.soma.everyonepick.login.api

import org.soma.everyonepick.login.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface UserService {
    @GET("api/user/me")
    suspend fun getUser(): UserResponse
}