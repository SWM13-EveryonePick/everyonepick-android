package org.soma.everyonepick.common.data.source

import okhttp3.MultipartBody
import org.soma.everyonepick.common.data.dto.FaceInfoResponse
import org.soma.everyonepick.common.data.dto.UserResponse
import retrofit2.http.*

interface UserService {
    @GET("api/v1/users/me")
    suspend fun readUser(@Header("Authorization") token: String): UserResponse

    @Multipart
    @POST("api/v1/users/face-info")
    suspend fun uploadFaceInfo(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): FaceInfoResponse
}