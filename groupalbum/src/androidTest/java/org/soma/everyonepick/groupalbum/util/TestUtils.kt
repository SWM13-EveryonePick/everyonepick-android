package org.soma.everyonepick.groupalbum.util

import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.model.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.model.PhotoDao

val testGroupAlbumDao = GroupAlbumDao(0, "test", 100)
val testGroupAlbumItem = GroupAlbumItem(testGroupAlbumDao, false, false)

val testPhotoDao = PhotoDao(0, "test")
val testPhotoItem = PhotoItem(testPhotoDao, false, false)