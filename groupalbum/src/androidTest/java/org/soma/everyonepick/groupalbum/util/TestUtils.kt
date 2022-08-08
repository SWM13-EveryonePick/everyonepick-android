package org.soma.everyonepick.groupalbum.util

import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.itemlist.GroupAlbumItemList
import org.soma.everyonepick.groupalbum.data.model.GroupAlbum
import org.soma.everyonepick.groupalbum.data.model.Photo

val testGroupAlbum = GroupAlbum(0, "test", 100)
val testGroupAlbumItem = GroupAlbumItem(testGroupAlbum, false, false)
val testGroupAlbumItemList = GroupAlbumItemList(mutableListOf(testGroupAlbumItem))

val testPhoto = Photo(0, "test")
val testPhotoItem = PhotoItem(testPhoto, false, false)