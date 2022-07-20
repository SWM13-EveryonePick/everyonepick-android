package org.soma.everyonepick.groupalbum.utility

import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem

val testGroupAlbumDao = GroupAlbumDao(0, "test")
val testGroupAlbumItem = GroupAlbumItem(testGroupAlbumDao, false, false)