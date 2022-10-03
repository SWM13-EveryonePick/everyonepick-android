package org.soma.everyonepick.groupalbum.domain.translator

import org.soma.everyonepick.groupalbum.data.entity.Pick
import org.soma.everyonepick.groupalbum.domain.model.PickModel

class PickTranslator {
    companion object {
        fun List<Pick>.toPickModelList() = this.map { PickModel(it.id, it.isDone, it.photo.photoUrl) }
    }
}