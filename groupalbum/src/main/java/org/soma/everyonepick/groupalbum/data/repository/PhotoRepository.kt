package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.model.Photo

class PhotoRepository {
    fun getPhotoItemList(groupAlbumId: Long): MutableList<PhotoItem> {
        // TODO: Retrofit2 -> photoList
        val photoList = mutableListOf(
            Photo(0, "https://picsum.photos/200"),
            Photo(1, "https://picsum.photos/201"),
            Photo(2, "https://picsum.photos/202"),
            Photo(3, "https://picsum.photos/203"),
            Photo(0, "https://picsum.photos/200"),
            Photo(1, "https://picsum.photos/201"),
            Photo(2, "https://picsum.photos/202"),
            Photo(3, "https://picsum.photos/203"),
            Photo(0, "https://picsum.photos/200"),
            Photo(1, "https://picsum.photos/201"),
            Photo(2, "https://picsum.photos/202"),
            Photo(3, "https://picsum.photos/203"),
            Photo(0, "https://picsum.photos/200"),
            Photo(1, "https://picsum.photos/201"),
            Photo(2, "https://picsum.photos/202"),
            Photo(3, "https://picsum.photos/203"),
            Photo(0, "https://picsum.photos/200"),
            Photo(1, "https://picsum.photos/201"),
            Photo(2, "https://picsum.photos/202"),
            Photo(3, "https://picsum.photos/203"),
        )
        return convertPhotoListToPhotoItemList(photoList)
    }

    fun convertPhotoListToPhotoItemList(photoList: MutableList<Photo>): MutableList<PhotoItem> {
        val photoItemList = mutableListOf<PhotoItem>()
        for(i in 0 until photoList.size) {
            photoItemList.add(PhotoItem(photoList[i], isChecked = false, isCheckboxVisible = false))
        }
        return photoItemList
    }
}