package org.soma.everyonepick.groupalbum.data.item

import com.kakao.sdk.talk.model.Friend

data class InviteFriendItem(
    val friend: Friend,
    var isChecked: Boolean
)