package org.soma.everyonepick.groupalbum.data.source.remote

import okhttp3.MultipartBody
import okhttp3.internal.http.hasBody
import org.soma.everyonepick.groupalbum.data.dto.*
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import retrofit2.http.*

interface GroupAlbumService {
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

    @POST ("api/album/{groupAlbumId}/user")
    suspend fun inviteUsersToGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @PATCH("api/album/{groupAlbumId}/user")
    suspend fun kickUsersOutOfGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @DELETE("api/album/{groupAlbumId}")
    suspend fun leaveGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): GroupAlbumResponse
}