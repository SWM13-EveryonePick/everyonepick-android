package org.soma.everyonepick.groupalbum.data.source.remote

import org.soma.everyonepick.groupalbum.data.dto.PickDetailResponse
import org.soma.everyonepick.groupalbum.data.dto.PickListResponse
import org.soma.everyonepick.groupalbum.data.dto.PickRequest
import org.soma.everyonepick.groupalbum.data.dto.PickResponse
import retrofit2.http.*

interface GroupAlbumPickService {
    @GET("api/album/{groupAlbumId}/pick")
    suspend fun readPickList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): PickListResponse

    @GET("api/album/{groupAlbumId}/pick/{pickId}")
    suspend fun readPick(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Path("pickId") pickId: Long
    ): PickDetailResponse

    @POST("api/album/{groupAlbumId}/pick")
    suspend fun createPick(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body pickRequest: PickRequest
    ): PickResponse
}