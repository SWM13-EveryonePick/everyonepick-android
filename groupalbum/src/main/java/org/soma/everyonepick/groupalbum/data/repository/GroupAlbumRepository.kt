package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.model.GroupAlbum

class GroupAlbumRepository {
    fun getGroupAlbumItemList(): MutableList<GroupAlbumItem> {
        // TODO: Retrofit2 -> groupAlbumList
        val groupAlbumList = mutableListOf(
            GroupAlbum(0, "title0", 0, 100),
            GroupAlbum(1, "title1", 1, 101),
            GroupAlbum(2, "title2", 2, 102),
            GroupAlbum(3, "title3", 3, 103)
        )
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
            id, "title$id", id, 100+id.toInt()
        )
    }
}