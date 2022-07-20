package org.soma.everyonepick.groupalbum.data

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupAlbumTest {
    private val groupAlbumDao = GroupAlbumDao(0, "id")
    @Test fun groupAlbumDaoListToGroupAlbumItemList() {
        val groupAlbumItemList = GroupAlbumRepository().convertGroupAlbumDaoListToGroupAlbumItemList(
            mutableListOf(groupAlbumDao)
        )
        assertEquals(groupAlbumItemList, mutableListOf(GroupAlbumItem(groupAlbumDao, false, false)))
    }
}