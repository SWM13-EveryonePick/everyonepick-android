package org.soma.everyonepick.camera.domain.usecase

import org.soma.everyonepick.camera.data.source.PoseService
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.camera.domain.translator.PoseTranslator.Companion.toPoseModelList
import javax.inject.Inject

class PoseUseCase @Inject constructor(
    private val poseService: PoseService
) {
    suspend fun readPoseList(token: String, posePackId: Long): MutableList<PoseModel> {
        return poseService.readPoseList(token, posePackId).data.toPoseModelList()
    }
}