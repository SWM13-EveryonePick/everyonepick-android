package org.soma.everyonepick.groupalbum.domain.usecase

import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import javax.inject.Inject

class PhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    fun getPhotoModelList(groupAlbumId: Long): MutableList<PhotoModel> {
        // TODO: photoRepository
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
        return photoList.toPhotoModelList()
    }

    private fun MutableList<Photo>.toPhotoModelList(): MutableList<PhotoModel> {
        val photoItemList = mutableListOf<PhotoModel>()
        for(i in 0 until size) {
            photoItemList.add(PhotoModel(get(i), isChecked = false, isCheckboxVisible = false))
        }
        return photoItemList
    }
}