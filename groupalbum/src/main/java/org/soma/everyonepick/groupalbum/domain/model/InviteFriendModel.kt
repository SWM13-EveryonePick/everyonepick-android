package org.soma.everyonepick.groupalbum.domain.model

import com.kakao.sdk.talk.model.Friend

data class InviteFriendModel(
    val friend: Friend,
    var isChecked: Boolean
)