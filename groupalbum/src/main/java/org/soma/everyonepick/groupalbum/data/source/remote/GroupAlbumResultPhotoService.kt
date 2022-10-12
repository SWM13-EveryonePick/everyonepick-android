package org.soma.everyonepick.groupalbum.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GroupAlbumResultPhotoService {
    @GET("api/album/{groupAlbumId}/pick/{pickId}/result")
    suspend fun readResultPhotoList(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long,
        @Path("pickId") pickId: Long
    )
}