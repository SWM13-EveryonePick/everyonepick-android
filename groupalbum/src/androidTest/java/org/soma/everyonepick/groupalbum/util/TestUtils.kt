package org.soma.everyonepick.groupalbum.util

import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.viewmodel.modellist.GroupAlbumModelList
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel

val testGroupAlbum = GroupAlbum(0, "test", 0, listOf(), 100)
val testGroupAlbumModel = GroupAlbumModel(testGroupAlbum, false, false)
val testGroupAlbumModelList = GroupAlbumModelList(mutableListOf(testGroupAlbumModel))

val testPhoto = Photo(0, "test")
val testPhotoItem = PhotoItem(testPhoto, false, false)