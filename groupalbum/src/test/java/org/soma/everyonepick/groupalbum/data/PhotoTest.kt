package org.soma.everyonepick.groupalbum.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.model.Photo
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository

class PhotoTest {
    private val photo = Photo(0, "url")

    @Test fun photoListToPhotoItemList() {
        val photoItemList = PhotoRepository().convertPhotoListToPhotoItemList(
            mutableListOf(photo)
        )
        assertEquals(photoItemList, mutableListOf(PhotoItem(photo, false, false)))
    }
}