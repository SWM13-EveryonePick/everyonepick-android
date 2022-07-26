package org.soma.everyonepick.groupalbum.utility

import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.PhotoDao
import org.soma.everyonepick.groupalbum.item.PhotoItem

val testGroupAlbumDao = GroupAlbumDao(0, "test", 100)
val testGroupAlbumItem = GroupAlbumItem(testGroupAlbumDao, false, false)

val testPhotoDao = PhotoDao(0, "test")
val testPhotoItem = PhotoItem(testPhotoDao, false, false)