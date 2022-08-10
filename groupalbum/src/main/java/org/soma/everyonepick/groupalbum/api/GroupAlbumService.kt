package org.soma.everyonepick.groupalbum.api

import org.soma.everyonepick.groupalbum.data.model.GroupAlbumListResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface GroupAlbumService {
    @GET("api/album")
    suspend fun getGroupAlbumList(@Header("Authorization") token: String): GroupAlbumListResponse
}