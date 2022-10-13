package org.soma.everyonepick.camera.domain.translator

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.camera.data.entity.Pose
import org.soma.everyonepick.camera.domain.model.PoseModel

class PoseTranslator {
    companion object {
        fun List<Pose>.toPoseModelList() = map {
            PoseModel(it, MutableStateFlow(false), true)
        }.toMutableList()
    }
}