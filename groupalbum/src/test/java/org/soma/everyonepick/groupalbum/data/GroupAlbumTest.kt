package org.soma.everyonepick.groupalbum.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.model.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository

class GroupAlbumTest {
    private val groupAlbumDao = GroupAlbumDao(0, "id", 100)
    @Test fun groupAlbumDaoListToGroupAlbumItemList() {
        val groupAlbumItemList = GroupAlbumRepository().convertGroupAlbumDaoListToGroupAlbumItemList(
            mutableListOf(groupAlbumDao)
        )
        assertFalse(groupAlbumItemList[0].isChecked)
        assertFalse(groupAlbumItemList[0].isCheckboxVisible)
    }
}