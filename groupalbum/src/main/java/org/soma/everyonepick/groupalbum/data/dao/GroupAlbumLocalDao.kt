package org.soma.everyonepick.groupalbum.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumLocal

@Dao
interface GroupAlbumLocalDao {
    @Query("SELECT * FROM group_album ORDER BY group_album.`index`")
    fun getGroupAlbumLocalList(): Flow<List<GroupAlbumLocal>>

    @Delete
    fun deleteGroupAlbumLocal(groupAlbumLocal: GroupAlbumLocal)

    @Insert
    suspend fun insertGroupAlbumLocal(groupAlbumLocal: GroupAlbumLocal)
}