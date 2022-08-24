package org.soma.everyonepick.groupalbum.data.entity

import androidx.room.*
import org.soma.everyonepick.common.data.entity.User

@Entity(tableName = "group_album")
data class GroupAlbumLocal(
    val index: Int,
    @PrimaryKey val id: Long,
    val title: String,
    val hostUserId: Long,
    val users: List<User?>,
    val photoCnt: Int
)