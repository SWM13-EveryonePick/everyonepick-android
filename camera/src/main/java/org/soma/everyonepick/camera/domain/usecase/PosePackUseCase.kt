package org.soma.everyonepick.camera.domain.usecase

import org.soma.everyonepick.camera.data.entity.PosePack
import org.soma.everyonepick.camera.data.repository.PosePackRepository
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.camera.domain.translator.PosePackTranslator.Companion.toPosePackModelList
import javax.inject.Inject

class PosePackUseCase @Inject constructor(
    private val posePackRepository: PosePackRepository
) {
    suspend fun readPosePackList(token: String): MutableList<PosePackModel> {
        return posePackRepository.readPosePackList(token).data.toPosePackModelList()
    }
}