package org.soma.everyonepick.groupalbum.data.source.remote

import okhttp3.MultipartBody
import okhttp3.internal.http.hasBody
import org.soma.everyonepick.groupalbum.data.dto.*
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import retrofit2.http.*

interface GroupAlbumService {
    @GET("api/v1/albums")
    suspend fun readGroupAlbumList(@Header("Authorization") token: String): GroupAlbumListResponse

    @POST("api/v1/albums")
    suspend fun createGroupAlbum(
        @Header("Authorization") token: String,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @GET("api/v1/albums/{groupAlbumId}")
    suspend fun readGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): GroupAlbumResponse

    @PATCH("api/v1/albums/{groupAlbumId}")
    suspend fun updateGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @POST ("api/v1/albums/{groupAlbumId}/user")
    suspend fun inviteUsersToGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @PATCH("api/v1/albums/{groupAlbumId}/user")
    suspend fun kickUsersOutOfGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body groupAlbum: GroupAlbum
    ): GroupAlbumResponse

    @DELETE("api/v1/albums/{groupAlbumId}")
    suspend fun leaveGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): GroupAlbumResponse
}