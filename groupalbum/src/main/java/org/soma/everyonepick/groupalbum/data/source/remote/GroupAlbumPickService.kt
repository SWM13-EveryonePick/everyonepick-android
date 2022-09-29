package org.soma.everyonepick.groupalbum.data.source.remote

import org.soma.everyonepick.groupalbum.data.dto.PickRequest
import org.soma.everyonepick.groupalbum.data.dto.PickResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroupAlbumPickService {
    @POST("api/album/{groupAlbumId}/pick")
    suspend fun createPick(
        @Header("Authorization") token: String,
        @Body pickRequest: PickRequest
    ): PickResponse
}