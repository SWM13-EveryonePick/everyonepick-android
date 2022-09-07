package org.soma.everyonepick.groupalbum.domain.translator

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel

class PhotoTranslator {
    companion object {
        fun MutableList<Photo>.toPhotoModelList(): MutableList<PhotoModel> {
            return this.map {
                PhotoModel(it, MutableStateFlow(false), isCheckboxVisible = false)
            }.toMutableList()
        }
    }
}