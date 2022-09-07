package org.soma.everyonepick.groupalbum.domain.model

import com.kakao.sdk.talk.model.Friend
import kotlinx.coroutines.flow.MutableStateFlow

data class InviteFriendModel(
    val friend: Friend,
    var isChecked: MutableStateFlow<Boolean>
)