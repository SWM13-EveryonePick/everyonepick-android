package org.soma.everyonepick.groupalbum.data.source.remote

import org.soma.everyonepick.groupalbum.data.dto.ResultPhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.dto.ResultPhotoListResponse
import retrofit2.http.*

interface GroupAlbumResultPhotoService {
    @GET("api/v1/albums/{groupAlbumId}/results")
    suspend fun readResultPhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): ResultPhotoListResponse

    // @DELETE에선 Body가 없는 것이 원칙이기 때문에 오류가 발생하였고, 아래 코드를 통해 허용할 수 있었습니다.
    @HTTP(method="DELETE", path="api/v1/albums/{groupAlbumId}/results", hasBody = true)
    suspend fun deleteResultPhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Body resultPhotos: ResultPhotoIdListRequest
    ): ResultPhotoListResponse
}