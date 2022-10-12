package org.soma.everyonepick.camera.domain.usecase

import org.soma.everyonepick.camera.data.entity.Pose
import org.soma.everyonepick.camera.data.source.PoseService
import javax.inject.Inject

class PoseUseCase @Inject constructor(
    private val poseService: PoseService
) {
    suspend fun readPoseList(token: String, peopleNum: String): MutableList<Pose> {
        return poseService.readPoseList(token, peopleNum).data.toMutableList()
    }
}