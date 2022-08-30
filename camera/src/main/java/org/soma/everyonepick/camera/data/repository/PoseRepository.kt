package org.soma.everyonepick.camera.data.repository

import org.soma.everyonepick.camera.data.dto.PoseListResponse
import org.soma.everyonepick.camera.data.entity.Pose

class PoseRepository {
    suspend fun readPoseList(token: String, posePackId: Long): PoseListResponse {
        // TODO: Retrofit2
        return PoseListResponse(
            "This is a temporary reponse.",
            listOf(
                Pose(0, "https://picsum.photos/200"),
                Pose(1, "https://picsum.photos/201"),
                Pose(2, "https://picsum.photos/202"),
                Pose(3, "https://picsum.photos/203"),
                Pose(4, "https://picsum.photos/204"),
                Pose(0, "https://picsum.photos/200"),
                Pose(1, "https://picsum.photos/201"),
                Pose(2, "https://picsum.photos/202"),
                Pose(3, "https://picsum.photos/203"),
                Pose(4, "https://picsum.photos/204"),
                Pose(0, "https://picsum.photos/200"),
                Pose(1, "https://picsum.photos/201"),
                Pose(2, "https://picsum.photos/202"),
                Pose(3, "https://picsum.photos/203"),
                Pose(4, "https://picsum.photos/204"),
                Pose(0, "https://picsum.photos/200"),
                Pose(1, "https://picsum.photos/201"),
                Pose(2, "https://picsum.photos/202"),
                Pose(3, "https://picsum.photos/203"),
                Pose(4, "https://picsum.photos/204"),
            ),
            ""
        )
    }
}