package org.soma.everyonepick.groupalbum.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.model.PhotoDao
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository

class PhotoTest {
    private val photoDao = PhotoDao(0, "url")

    @Test fun photoDaoListToPhotoItemList() {
        val photoItemList = PhotoRepository().convertPhotoDaoListToPhotoItemList(
            mutableListOf(photoDao)
        )
        assertEquals(photoItemList, mutableListOf(PhotoItem(photoDao, false, false)))
    }
}