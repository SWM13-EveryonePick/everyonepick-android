package org.soma.everyonepick.camera.data.source

import org.soma.everyonepick.camera.data.dto.PoseListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PoseService {
    @GET("api/v1/poses")
    suspend fun readPoseList(
        @Header("Authorization") token: String,
        @Query("peopleNum") peopleNum: String
    ): PoseListResponse
}