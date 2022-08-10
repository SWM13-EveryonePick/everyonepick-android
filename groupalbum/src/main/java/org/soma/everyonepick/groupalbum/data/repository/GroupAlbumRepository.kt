package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumCreateRequest
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumListResponse
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetailDto
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumResponse
import retrofit2.http.*

interface GroupAlbumRepository {
    @GET("api/album")
    suspend fun getGroupAlbumList(@Header("Authorization") token: String): GroupAlbumListResponse

    @POST("api/album")
    suspend fun createGroupAlbum(
        @Header("Authorization") token: String,
        @Body groupAlbumCreateRequest: GroupAlbumCreateRequest
    ): GroupAlbumResponse

    @GET("api/album/{groupAlbumId}")
    suspend fun getGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): GroupAlbumResponse
}