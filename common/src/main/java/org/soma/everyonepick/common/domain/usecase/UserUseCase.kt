package org.soma.everyonepick.common.domain.usecase

import okhttp3.MultipartBody
import org.soma.everyonepick.common.data.dto.FaceInfoResponse
import org.soma.everyonepick.common.data.source.UserService
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userService: UserService
) {
    suspend fun readUser(token: String) = userService.readUser(token).data
    suspend fun uploadFaceInfo(token: String, image: MultipartBody.Part): FaceInfoResponse {
        return userService.uploadFaceInfo(token, image)
    }
}