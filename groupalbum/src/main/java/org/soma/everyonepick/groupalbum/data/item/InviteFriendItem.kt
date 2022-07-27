package org.soma.everyonepick.groupalbum.data.item

import com.kakao.sdk.talk.model.Friend

// Invite Friend Recycler View에 들어가는 Item 객체입니다.
data class InviteFriendItem(
    val friend: Friend,
    var isChecked: Boolean
)