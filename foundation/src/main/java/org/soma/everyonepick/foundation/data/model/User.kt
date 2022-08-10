package org.soma.everyonepick.foundation.data.model

data class User(
    val id: Long,
    val nickname: String,
    val clientId: String,
    val thumbnailImageUrl: String,
    val isPushActive: Boolean
)
