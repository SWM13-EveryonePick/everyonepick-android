package org.soma.everyonepick.groupalbum.data.source.remote

import okhttp3.MultipartBody
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListResponse
import org.soma.everyonepick.groupalbum.data.dto.PhotoListResponse
import retrofit2.http.*

interface GroupAlbumPhotoService {
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