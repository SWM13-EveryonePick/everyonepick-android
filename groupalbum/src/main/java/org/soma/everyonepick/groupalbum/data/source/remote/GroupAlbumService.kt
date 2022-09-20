package org.soma.everyonepick.groupalbum.data.source.remote

import okhttp3.MultipartBody
import okhttp3.internal.http.hasBody
import org.soma.everyonepick.groupalbum.data.dto.*
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
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

    @GET("api/album/{groupAlbumId}/photo")
    suspend fun readPhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): PhotoListResponse

    @Multipart
    @POST("api/album/{groupAlbumId}/photo")
    suspend fun createPhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Part images: List<MultipartBody.Part>
    ): PhotoListResponse

    // @DELETE에선 Body가 없는 것이 원칙이기 때문에 오류가 발생하였고, 아래 코드를 통해 허용할 수 있었습니다.
    @HTTP(method="DELETE", path="api/album/{groupAlbumId}/photo", hasBody = true)
    suspend fun deletePhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body photos: PhotoIdListRequest
    ): PhotoIdListResponse
}