package org.soma.everyonepick.camera.domain.usecase

import org.soma.everyonepick.camera.data.source.PosePackService
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.camera.domain.translator.PosePackTranslator.Companion.toPosePackModelList
import javax.inject.Inject

class PosePackUseCase @Inject constructor(
    private val posePackService: PosePackService
) {
    suspend fun readPosePackList(token: String): MutableList<PosePackModel> {
        return posePackService.readPosePackList(token).data.toPosePackModelList()
    }
}