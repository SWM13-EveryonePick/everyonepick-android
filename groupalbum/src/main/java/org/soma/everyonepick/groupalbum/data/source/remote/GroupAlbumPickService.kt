package org.soma.everyonepick.groupalbum.data.source.remote

import org.soma.everyonepick.groupalbum.data.dto.*
import retrofit2.http.*

interface GroupAlbumPickService {
    @GET("api/v1/albums/{groupAlbumId}/picks")
    suspend fun readPickList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): PickListResponse

    @GET("api/v1/albums/{groupAlbumId}/picks/{pickId}")
    suspend fun readPick(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Path("pickId") pickId: Long
    ): PickDetailResponse

    @POST("api/v1/albums/{groupAlbumId}/picks")
    suspend fun createPick(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body pickRequest: PickRequest
    ): PickResponse


    /** Pick info */
    @GET("api/v1/picks/{pickId}/pick-info")
    suspend fun readPickInfo(
        @Header("Authorization") token: String,
        @Path("pickId") pickId: Long
    ): PickInfoResponse

    @POST("api/v1/picks/{pickId}/pick-info")
    suspend fun createPickInfo(
        @Header("Authorization") token: String,
        @Path("pickId") pickId: Long,
        @Body photos: PhotoIdListRequest
    ): PickInfoResponse
}