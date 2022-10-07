package org.soma.everyonepick.groupalbum.domain.model

data class PickInfoModel(
    val userCount: Int,
    val pickUserCount: Int,
    var timeOut: Long // 초 단위
)