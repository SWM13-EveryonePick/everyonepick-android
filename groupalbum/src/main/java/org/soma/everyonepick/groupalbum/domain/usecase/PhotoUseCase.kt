package org.soma.everyonepick.groupalbum.domain.usecase

import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import org.soma.everyonepick.groupalbum.domain.translator.PhotoTranslator.Companion.toPhotoModelList
import javax.inject.Inject

class PhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    fun readPhotoModelList(groupAlbumId: Long): MutableList<PhotoModel> {
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
}