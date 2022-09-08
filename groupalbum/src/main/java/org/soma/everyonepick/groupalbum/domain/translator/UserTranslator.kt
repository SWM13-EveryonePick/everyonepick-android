package org.soma.everyonepick.groupalbum.domain.translator

import com.kakao.sdk.talk.model.Friend
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.util.KakaoUtil.Companion.toUserWithClientId

/**
 * [User.clientId]의 내용만 있는 User 리스트를 반환합니다.
 */
fun MutableList<Friend>.toUserListWithClientId(): MutableList<User> {
    return map { it.toUserWithClientId() }.toMutableList()
}