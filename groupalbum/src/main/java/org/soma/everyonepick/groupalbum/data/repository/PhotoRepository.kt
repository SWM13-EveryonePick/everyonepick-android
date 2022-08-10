package org.soma.everyonepick.groupalbum.data.repository

import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.data.entity.Photo
import javax.inject.Singleton

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
        return photoList.toPhotoItemList()
    }

    private fun MutableList<Photo>.toPhotoItemList(): MutableList<PhotoItem> {
        val photoItemList = mutableListOf<PhotoItem>()
        for(i in 0 until size) {
            photoItemList.add(PhotoItem(get(i), isChecked = false, isCheckboxVisible = false))
        }
        return photoItemList
    }
}