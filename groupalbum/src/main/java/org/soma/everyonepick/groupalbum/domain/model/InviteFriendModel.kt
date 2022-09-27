package org.soma.everyonepick.groupalbum.domain.model

import com.kakao.sdk.talk.model.Friend
import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.domain.Checkable

data class InviteFriendModel(
    val friend: Friend,
    override var isChecked: MutableStateFlow<Boolean>
): Checkable