package org.soma.everyonepick.common.data.entity

data class User(
    val id: Long?,
    val nickname: String?,
    val clientId: String?,
    val thumbnailImageUrl: String?,
    val isPushActive: Boolean?,
    val isRegistered: Boolean?
) {
    companion object {
        val dummyData = User(-1, "", "", "", false, true)
    }
}