package org.soma.everyonepick.groupalbum.data.repository

import kotlinx.coroutines.flow.first
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
        groupAlbumLocalList.forEach {
            groupAlbumLocalDao.insertGroupAlbumLocal(it)
        }

        // 저장되어 있는 아이템들이 새 리스트 내부에 없다면 삭제합니다.
        val prev = getGroupAlbumLocalList().first()
        prev.forEach { prevItem ->
            if (!groupAlbumLocalList.any { it.id == prevItem.id }) {
                groupAlbumLocalDao.deleteGroupAlbumLocal(prevItem)
            }
        }
    }
}