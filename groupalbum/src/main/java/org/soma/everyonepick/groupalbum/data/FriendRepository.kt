package org.soma.everyonepick.groupalbum.data

import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends

class FriendRepository {
    /* TODO:
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
            }else if (friends != null){
                Log.i(TAG, "카카오톡 친구 목록 가져오기 성공")
            }
        }
     */
    fun getFriends(): Friends<Friend>? {
        return Friends(
            1,
            listOf(
                Friend(
                    0,
                    "UUID",
                    "Nickname",
                    "https://picsum.photos/200",
                    true,
                    true,
                    0
                )
            ),
            1,
            null,
            null
        )
    }
}