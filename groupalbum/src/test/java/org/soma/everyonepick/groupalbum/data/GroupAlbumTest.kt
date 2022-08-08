package org.soma.everyonepick.groupalbum.data

import org.junit.Assert.assertFalse
import org.junit.Test
import org.soma.everyonepick.groupalbum.data.model.GroupAlbum
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository

class GroupAlbumTest {
    private val groupAlbum = GroupAlbum(0, "id", 100)
    @Test fun groupAlbumDaoListToGroupAlbumItemList() {
        val groupAlbumItemList = GroupAlbumRepository().convertGroupAlbumListToGroupAlbumItemList(
            mutableListOf(groupAlbum)
        )
        assertFalse(groupAlbumItemList[0].isChecked)
        assertFalse(groupAlbumItemList[0].isCheckboxVisible)
    }
}