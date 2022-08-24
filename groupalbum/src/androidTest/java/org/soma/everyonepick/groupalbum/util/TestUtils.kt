package org.soma.everyonepick.groupalbum.util

import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetail
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.modellist.GroupAlbumModelList
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadList
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel

val testGroupAlbum = GroupAlbumReadList(0, "test", 0, listOf(), 100)
val testGroupAlbumModel = GroupAlbumModel(testGroupAlbum, false, false)
val testGroupAlbumModelList = GroupAlbumModelList(mutableListOf(testGroupAlbumModel))

val testPhoto = Photo(0, "test")
val testPhotoModel = PhotoModel(testPhoto, false, false)

val testGroupAlbumReadDetail = GroupAlbumReadDetail(0, "test", 0, listOf(User.dummyData), listOf())