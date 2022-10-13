package org.soma.everyonepick.groupalbum.data.entity

import com.google.gson.annotations.SerializedName

data class PickInfo(
    val userCnt: Int,
    val pickUserCnt: Int,
    @SerializedName("timeOut")
    val timeout: Long
)
