package org.soma.everyonepick.camera.domain.usecase

import org.soma.everyonepick.camera.data.repository.PoseRepository
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.camera.domain.translator.PoseTranslator.Companion.toPoseModelList
import javax.inject.Inject

class PoseUseCase @Inject constructor(
    private val poseRepository: PoseRepository
) {
    suspend fun readPoseList(token: String, posePackId: Long): MutableList<PoseModel> {
        return poseRepository.readPoseList(token, posePackId).data.toPoseModelList()
    }
}