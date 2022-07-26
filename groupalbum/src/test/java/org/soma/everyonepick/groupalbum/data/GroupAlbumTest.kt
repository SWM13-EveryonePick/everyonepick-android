package org.soma.everyonepick.groupalbum.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.soma.everyonepick.groupalbum.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.repository.GroupAlbumRepository

class GroupAlbumTest {
    private val groupAlbumDao = GroupAlbumDao(0, "id", 100)
    @Test fun groupAlbumDaoListToGroupAlbumItemList() {
        val groupAlbumItemList = GroupAlbumRepository().convertGroupAlbumDaoListToGroupAlbumItemList(
            mutableListOf(groupAlbumDao)
        )
        assertEquals(groupAlbumItemList, mutableListOf(GroupAlbumItem(groupAlbumDao, false, false)))
    }
}