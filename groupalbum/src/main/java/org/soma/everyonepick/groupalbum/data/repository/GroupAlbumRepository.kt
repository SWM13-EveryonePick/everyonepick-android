package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.common.api.RetrofitFactory.Companion.toBearerToken
import org.soma.everyonepick.groupalbum.api.GroupAlbumService
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.model.GroupAlbum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupAlbumRepository @Inject constructor(
    private val groupAlbumService: GroupAlbumService
) {
    suspend fun getGroupAlbumItemList(token: String): MutableList<GroupAlbumItem> {
        val groupAlbumList = groupAlbumService.getGroupAlbumList(token.toBearerToken()).data.toMutableList()
        return groupAlbumList.toGroupAlbumItemList()
    }

    private fun MutableList<GroupAlbum>.toGroupAlbumItemList(): MutableList<GroupAlbumItem> {
        val groupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until size) {
            groupAlbumItemList.add(GroupAlbumItem(get(i), isChecked = false, isCheckboxVisible = false))
        }
        return groupAlbumItemList
    }

    fun getGroupAlbum(id: Long): GroupAlbum {
        // TODO: Retrofit2... req: id -> res: GroupAlbum
        return GroupAlbum(
            id, "title$id", id, listOf(), 100+id.toInt()
        )
    }
}