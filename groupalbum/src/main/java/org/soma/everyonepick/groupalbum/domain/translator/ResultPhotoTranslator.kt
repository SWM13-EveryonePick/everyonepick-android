package org.soma.everyonepick.groupalbum.domain.translator

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.ResultPhoto
import org.soma.everyonepick.groupalbum.domain.model.ResultPhotoModel

class ResultPhotoTranslator {
    companion object {
        fun List<ResultPhoto>.toResultPhotoModelList(): MutableList<ResultPhotoModel> {
            return this.map {
                ResultPhotoModel(it, MutableStateFlow(false), isCheckboxVisible = false)
            }.toMutableList()
        }
    }
}