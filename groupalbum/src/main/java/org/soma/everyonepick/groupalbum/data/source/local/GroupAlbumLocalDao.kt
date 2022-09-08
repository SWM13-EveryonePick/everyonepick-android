package org.soma.everyonepick.groupalbum.data.source.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumLocal

@Dao
interface GroupAlbumLocalDao {
    @Query("SELECT * FROM group_album ORDER BY group_album.`index`")
    fun getGroupAlbumLocalList(): List<GroupAlbumLocal>

    @Query("DELETE FROM group_album")
    suspend fun deleteGroupAlbumTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupAlbumLocal(groupAlbumLocal: GroupAlbumLocal)
}