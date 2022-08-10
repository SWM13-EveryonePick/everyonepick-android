package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import javax.inject.Inject
import javax.inject.Singleton

interface GroupAlbumRepository {
    @GET("api/album")
    suspend fun getGroupAlbumList(@Header("Authorization") token: String): GroupAlbumListResponse
}