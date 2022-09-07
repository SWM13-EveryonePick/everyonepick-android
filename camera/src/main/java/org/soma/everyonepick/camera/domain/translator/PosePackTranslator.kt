package org.soma.everyonepick.camera.domain.translator

import org.soma.everyonepick.camera.data.entity.PosePack
import org.soma.everyonepick.camera.domain.model.PosePackModel

class PosePackTranslator {
    companion object {
        fun List<PosePack>.toPosePackModelList() = map {
            PosePackModel(it.id, it.name)
        }.toMutableList()
    }
}