package org.soma.everyonepick.foundation.data.dao

data class User(
    val PK: Long,
    val username: String,
    val kakao_uuid: Long?,
    val face_feature: String?,
    val fcm_device_token: String?,
    val is_push_active: Boolean,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String?
)
