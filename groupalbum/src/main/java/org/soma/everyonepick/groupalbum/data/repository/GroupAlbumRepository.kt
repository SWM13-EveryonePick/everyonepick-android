package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumListResponse
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

interface GroupAlbumRepository {
    @GET("api/album")
    suspend fun getGroupAlbumList(@Header("Authorization") token: String): GroupAlbumListResponse

    @GET("api/album/{groupAlbumId}")
    suspend fun getGroupAlbum(
        @Header("Authorization") token: String,
        @Path("groupAlbumId") groupAlbumId: Long
    ): GroupAlbumResponse
}