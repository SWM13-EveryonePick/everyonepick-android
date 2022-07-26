package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.model.PhotoDao

class PhotoRepository {
    fun getPhotoItemList(groupAlbumId: Long): MutableList<PhotoItem> {
        // TODO: Retrofit2 -> photoDaoList
        val photoDaoList = mutableListOf(
            PhotoDao(0, "https://picsum.photos/200"),
            PhotoDao(1, "https://picsum.photos/201"),
            PhotoDao(2, "https://picsum.photos/202"),
            PhotoDao(3, "https://picsum.photos/203")
        )
        return convertPhotoDaoListToPhotoItemList(photoDaoList)
    }

    fun convertPhotoDaoListToPhotoItemList(photoDaoList: MutableList<PhotoDao>): MutableList<PhotoItem> {
        val photoItemList = mutableListOf<PhotoItem>()
        for(i in 0 until photoDaoList.size) {
            photoItemList.add(PhotoItem(photoDaoList[i], isChecked = false, isCheckboxVisible = false))
        }
        return photoItemList
    }
}