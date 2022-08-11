package org.soma.everyonepick.groupalbum.domain.translator

import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel

class PhotoTranslator {
    companion object {
        fun MutableList<Photo>.toPhotoModelList(): MutableList<PhotoModel> {
            val photoModelList = mutableListOf<PhotoModel>()
            for(i in 0 until size) {
                photoModelList.add(PhotoModel(get(i), isChecked = false, isCheckboxVisible = false))
            }
            return photoModelList
        }
    }
}