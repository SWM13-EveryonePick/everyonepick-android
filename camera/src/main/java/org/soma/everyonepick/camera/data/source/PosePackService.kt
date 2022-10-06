package org.soma.everyonepick.camera.data.source

import org.soma.everyonepick.camera.data.dto.PosePackListResponse
import org.soma.everyonepick.camera.data.entity.PosePack

class PosePackService {
    suspend fun readPosePackList(token: String): PosePackListResponse {
        // TODO: Retrofit2
        return PosePackListResponse(
            "This is a temporary reponse.",
            listOf(
                PosePack(0, "Favorite"),
                PosePack(1, "3인"),
                PosePack(2, "4인"),
                PosePack(3, "5인"),
                PosePack(4, "6인"),
                PosePack(5, "7인"),
            ),
            0
        )
    }
}