package org.soma.everyonepick.groupalbum.data.source.remote

import org.soma.everyonepick.groupalbum.data.dto.ResultPhotoListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GroupAlbumResultPhotoService {
    @GET("api/album/{groupAlbumId}/result")
    suspend fun readResultPhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): ResultPhotoListResponse
}