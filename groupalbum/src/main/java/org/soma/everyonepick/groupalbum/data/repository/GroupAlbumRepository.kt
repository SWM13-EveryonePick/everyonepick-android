package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.dto.GroupAlbumListResponse
import org.soma.everyonepick.groupalbum.data.dto.GroupAlbumResponse
import retrofit2.http.*

interface GroupAlbumRepository {
    @GET("api/album")
    suspend fun readGroupAlbumList(@Header("Authorization") token: String): GroupAlbumListResponse

    @POST("api/album")
    suspend fun createGroupAlbum(
        @Header("Authorization") token: String,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @GET("api/album/{groupAlbumId}")
    suspend fun readGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): GroupAlbumResponse

    @PATCH("api/album/{groupAlbumId}")
    suspend fun updateGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse
}