package org.soma.everyonepick.common.util

import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import org.soma.everyonepick.common.data.entity.User

class KakaoUtil {
    companion object {
        val emptyFriends = Friends<Friend>(0, listOf(), 0, "", "")
        val manyFriends = Friends(17, listOf(
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(2, "", "Shumann", "https://picsum.photos/202", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
            Friend(0, "", "이주연", "https://picsum.photos/200", favorite = false, allowedMsg = false, 0),
            Friend(1, "", "Rachmaninoff", "https://picsum.photos/201", favorite = false, allowedMsg = false, 0),
        ), 0, "","")

        fun Friend.toUserWithClientId() = User(null, null, "kakao_$id", null, null)
    }
}