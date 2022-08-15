package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.dao.GroupAlbumLocalDao
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumLocal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupAlbumLocalRepository @Inject constructor(
    private val groupAlbumLocalDao: GroupAlbumLocalDao
) {
    fun getGroupAlbumLocalList() = groupAlbumLocalDao.getGroupAlbumLocalList()

    suspend fun resetGroupAlbumLocalList(groupAlbumLocalList: List<GroupAlbumLocal>) {
        groupAlbumLocalDao.deleteGroupAlbumTable()
        groupAlbumLocalList.forEach {
            groupAlbumLocalDao.insertGroupAlbumLocal(it)
        }
    }
}